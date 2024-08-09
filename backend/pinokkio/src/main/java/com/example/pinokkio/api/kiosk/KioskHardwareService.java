package com.example.pinokkio.api.kiosk;

import com.example.pinokkio.api.customer.FaceAnalysisService;
import com.example.pinokkio.api.customer.sse.SSEService;
import com.example.pinokkio.grpc.*;
import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class KioskHardwareService extends KioskServiceGrpc.KioskServiceImplBase {


    // 거리 임계값을 설정합니다. 이 값 이하의 거리일 때 동작을 수행합니다.
    private static final double DISTANCE_THRESHOLD = 80.0;
    // 안정적인 거리 측정 횟수 임계값을 설정합니다.
    private static final int STABLE_COUNT_THRESHOLD = 5;
    // 최대 밝기 값을 설정합니다.
    private static final int MAX_BRIGHTNESS = 100;
    // 타임아웃 시간을 밀리초 단위로 설정합니다.
    private static final long TIMEOUT_DURATION = 5000;
    // 키오스크 컨트롤러의 주소를 설정합니다.
    private static final String KIOSK_CONTROLLER_ADDRESS = "70.12.114.81";
    // 키오스크 컨트롤러의 포트를 설정합니다.
    private static final int KIOSK_CONTROLLER_PORT = 3334;
    // gRPC 서버의 포트를 설정합니다.
    private static final int GRPC_SERVER_PORT = 3333;

    // 각 키오스크의 마지막 측정 거리를 저장하는 맵입니다.
    private Map<String, Double> lastDistances = new ConcurrentHashMap<>();
    // 각 키오스크의 안정적인 거리 측정 횟수를 저장하는 맵입니다.
    private Map<String, Integer> stableCount = new ConcurrentHashMap<>();
    // 각 키오스크의 마지막 활동 시간을 저장하는 맵입니다.
    private Map<String, Instant> lastActivityTime = new ConcurrentHashMap<>();

    // gRPC 서버 객체입니다.
    private Server grpcServer;

    private final SSEService sseService;
    private final FaceAnalysisService faceAnalysisService;
    private final KioskService kioskService;

    // gRPC 서버를 시작하는 메서드입니다. 애플리케이션 시작 시 자동으로 실행됩니다.
    @PostConstruct
    public void startGrpcServer() {
        try {
            grpcServer = ServerBuilder.forPort(GRPC_SERVER_PORT)
                    .addService(this)
                    .build()
                    .start();
            log.info("gRPC server started on port " + GRPC_SERVER_PORT);
        } catch (IOException e) {
            log.error("Failed to start gRPC server", e);
        }
    }

    // gRPC 서버를 중지하는 메서드입니다. 애플리케이션 종료 시 자동으로 실행됩니다.
    @PreDestroy
    public void stopGrpcServer() {
        if (grpcServer != null) {
            grpcServer.shutdown();
            try {
                if (!grpcServer.awaitTermination(5, TimeUnit.SECONDS)) {
                    grpcServer.shutdownNow();
                }
            } catch (InterruptedException e) {
                grpcServer.shutdownNow();
            }
        }
    }

    // 거리 데이터를 받아 처리하는 gRPC 메서드입니다.
    @Override
    public void receiveDistanceData(DistanceData request, StreamObserver<Empty> responseObserver) {
        String kioskId = request.getKioskId();
        double distance = request.getDistance();
        log.info("Received distance data from kiosk {}: {} cm", kioskId, distance);
        boolean shouldProcessFurther = processDistanceData(kioskId, distance);
        if (shouldProcessFurther) {
            handleUserDetected(kioskId);
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private void handleUserDetected(String kioskId) {
        log.info("User detected at kiosk {}. Initiating brightness adjustment and image capture.", kioskId);

        stopDistanceMeasurement(kioskId)
                .thenCompose(v -> adjustBrightness(kioskId, MAX_BRIGHTNESS))
                .thenCompose(v -> captureAndAnalyzeImages(kioskId, 2))
                .exceptionally(e -> {
                    log.error("Error in user detection process for kiosk {}", kioskId, e);
                    return null;
                });
    }

    public CompletableFuture<Void> stopDistanceMeasurement(String kioskId) {
        return CompletableFuture.runAsync(() -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(KIOSK_CONTROLLER_ADDRESS, KIOSK_CONTROLLER_PORT)
                    .usePlaintext()
                    .build();
            KioskServiceGrpc.KioskServiceBlockingStub stub = KioskServiceGrpc.newBlockingStub(channel);

            StopDistanceMeasurementRequest request = StopDistanceMeasurementRequest.newBuilder()
                    .setKioskId(kioskId)
                    .build();

            try {
                stub.stopDistanceMeasurement(request);
                log.info("Stopped distance measurement for kiosk: {}", kioskId);
            } catch (StatusRuntimeException e) {
                log.error("Error stopping distance measurement for kiosk: {}", kioskId, e);
            } finally {
                log.info("Shutdown!");
                channel.shutdown();
            }
        });
    }

    // 키오스크의 밝기를 조절하는 메서드입니다.
    public CompletableFuture<Void> adjustBrightness(String kioskId, int brightness) {
        return CompletableFuture.runAsync(() -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(KIOSK_CONTROLLER_ADDRESS, KIOSK_CONTROLLER_PORT)
                    .usePlaintext()
                    .build();
            KioskServiceGrpc.KioskServiceFutureStub stub = KioskServiceGrpc.newFutureStub(channel);

            BrightnessRequest request = BrightnessRequest.newBuilder()
                    .setBrightness(brightness)
                    .build();

            try {
                stub.setBrightness(request).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error adjusting brightness", e);
            } finally {
                channel.shutdown();
            }
        });
    }

    // 이미지를 캡처하고 분석하는 메서드입니다.
    public CompletableFuture<Object> captureAndAnalyzeImages(String kioskId, int count) {
        return CompletableFuture.supplyAsync(() -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(KIOSK_CONTROLLER_ADDRESS, KIOSK_CONTROLLER_PORT)
                    .usePlaintext()
                    .build();
            KioskServiceGrpc.KioskServiceBlockingStub stub = KioskServiceGrpc.newBlockingStub(channel);

            CaptureImagesRequest request = CaptureImagesRequest.newBuilder()
                    .setCount(count)
                    .build();

            try {
                CaptureImagesResponse response = stub.captureImages(request);
                List<ByteString> capturedImages = response.getImagesList();
                if (!capturedImages.isEmpty()) {
                    sseService.sendWaitingEvent(true);
                    List<String> base64Images = capturedImages.stream()
                            .map(ByteString::toStringUtf8)
                            .collect(Collectors.toList());
                    faceAnalysisService.analyzeImages(base64Images);
                    return null;
                } else {
                    log.error("Failed to capture images from kiosk: {}", kioskId);
                    sseService.sendWaitingEvent(false);
                    throw new RuntimeException("Failed to capture images");
                }
            } catch (Exception e) {
                log.error("Error during image capture or analysis for kiosk: {}", kioskId, e);
                sseService.sendWaitingEvent(false);
                resetKiosk(kioskId).join(); // kioskReset 호출
                return null;
            } finally {
                channel.shutdown();
            }
        });
    }

    // 거리 데이터를 처리하는 메서드입니다.
    public boolean processDistanceData(String kioskId, double distance) {
        lastActivityTime.put(kioskId, Instant.now());

        if (isStableDistance(kioskId, distance)) {
            adjustBrightness(kioskId, MAX_BRIGHTNESS);
            sseService.sendWaitingEvent(true);
            return true;
        }

        if (isTimeout(kioskId)) {
            resetKiosk(kioskId);
            sseService.sendWaitingEvent(false);
        }

        return false;
    }

    // 거리가 안정적인지 확인하는 메서드입니다.
    private boolean isStableDistance(String kioskId, double distance) {
        if (distance > DISTANCE_THRESHOLD) {
            stableCount.remove(kioskId);
            return false;
        }

        Double lastDistance = lastDistances.get(kioskId);
        if (lastDistance != null && Math.abs(distance - lastDistance) > 5.0) {
            stableCount.remove(kioskId);
        } else {
            int count = stableCount.getOrDefault(kioskId, 0) + 1;
            stableCount.put(kioskId, count);
            if (count >= STABLE_COUNT_THRESHOLD) {
                stableCount.remove(kioskId);
                return true;
            }
        }

        lastDistances.put(kioskId, distance);
        return false;
    }

    // 타임아웃 여부를 확인하는 메서드입니다.
    private boolean isTimeout(String kioskId) {
        Instant lastActivity = lastActivityTime.get(kioskId);
        if (lastActivity == null) {
            return false;
        }
        return Instant.now().minusMillis(TIMEOUT_DURATION).isAfter(lastActivity);
    }

    // 키오스크를 리셋하는 메서드입니다.
    public CompletableFuture<Void> resetKiosk(String kioskId) {
        return CompletableFuture.runAsync(() -> {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(KIOSK_CONTROLLER_ADDRESS, KIOSK_CONTROLLER_PORT)
                    .usePlaintext()
                    .build();
            KioskServiceGrpc.KioskServiceBlockingStub stub = KioskServiceGrpc.newBlockingStub(channel);

            ResetRequest request = ResetRequest.newBuilder().build();

            try {
                stub.resetKiosk(request);
                log.info("Kiosk reset successful: {}", kioskId);
                sseService.sendWaitingEvent(false);
            } catch (StatusRuntimeException e) {
                log.error("Error resetting kiosk: {}", kioskId, e);
            } finally {
                channel.shutdown();
            }
        });
    }

    // 키오스크에 제어 신호를 보내는 메서드입니다.
    public void sendControlSignal(String kioskId, String signal) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(KIOSK_CONTROLLER_ADDRESS, KIOSK_CONTROLLER_PORT)
                .usePlaintext()
                .build();
        KioskServiceGrpc.KioskServiceBlockingStub stub = KioskServiceGrpc.newBlockingStub(channel);

        ControlSignalRequest request = ControlSignalRequest.newBuilder()
                .setSignal(signal)
                .build();

        try {
            stub.sendControlSignal(request);
            log.info("Control signal sent successfully: {} to kiosk: {}", signal, kioskId);
            sseService.sendWaitingEvent(true);
        } catch (StatusRuntimeException e) {
            log.error("Error sending control signal: {} to kiosk: {}", signal, kioskId, e);
            sseService.sendWaitingEvent(false);
        } finally {
            channel.shutdown();
        }
    }

    // gRPC 로그인 처리 메서드입니다.
    @Override
    public void kioskLogin(KioskId request, StreamObserver<LoginResponse> responseObserver) {
        LoginResponse response = kioskService.handleGrpcLogin(UUID.fromString(request.getId()));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
