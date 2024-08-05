package com.example.pinokkio.api.customer.sse;

import com.example.pinokkio.api.customer.Customer;
import com.example.pinokkio.api.customer.dto.response.AnalysisResult;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SSEService {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    // 여러 클라이언트의 SseEmitter를 관리하기 위한 스레드 안전한 리스트입니다.
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SSEService() {
        scheduler.scheduleAtFixedRate(this::sendKeepAlive, 0, 15, TimeUnit.SECONDS);
    }

    private void sendKeepAlive() {
        Map<String, Object> keepAliveData = new HashMap<>();
        keepAliveData.put("status", "keep-alive");
        sendEventToAll("keepAlive", keepAliveData);
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
        emitters.forEach(SseEmitter::complete);
    }

    // 새로운 SSE 연결을 생성하는 메서드입니다.
    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected successfully"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    // 대기 상태 변경 이벤트를 전송하는 메서드입니다.
    public void sendWaitingEvent(boolean isWaiting) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("waiting", isWaiting);
        sendEventToAll("waitingStatus", eventData);
    }

    // 얼굴 감지 결과를 전송하는 메서드입니다.
    public void sendFaceDetectionResult(boolean isFaceDetected) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("isFace", isFaceDetected);
        sendEventToAll("faceDetectionResult", eventData);
    }

    // 얼굴 분석 결과와 고객 정보를 전송하는 메서드입니다.
    public void sendAnalysisResult(AnalysisResult analysisResult, Customer customer) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("age", analysisResult.getAge());
        eventData.put("gender", analysisResult.getGender());
        eventData.put("isFace", analysisResult.isFace());
        eventData.put("isCustomer", customer != null);

        if (customer != null) {
            eventData.put("customerId", customer.getId());
            eventData.put("customerAge", customer.getAge());
            eventData.put("customerGender", customer.getGender());
        }

        sendEventToAll("analysisResult", eventData);
    }

    // 모든 연결된 클라이언트에게 이벤트를 전송하는 private 메서드입니다.
    private void sendEventToAll(String eventName, Map<String, Object> eventData) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : this.emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(eventData));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }

        this.emitters.removeAll(deadEmitters);
    }

}
