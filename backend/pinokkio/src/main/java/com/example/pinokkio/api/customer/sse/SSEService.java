package com.example.pinokkio.api.customer.sse;

import com.example.pinokkio.api.customer.Customer;
import com.example.pinokkio.api.customer.dto.response.AnalysisResult;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SSEService {

    // 여러 클라이언트의 SseEmitter를 관리하기 위한 스레드 안전한 리스트입니다.
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // 새로운 SSE 연결을 생성하는 메서드입니다.
    public SseEmitter createEmitter() {
        // 무한 타임아웃으로 SseEmitter를 생성합니다.
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        // 생성된 emitter를 리스트에 추가합니다.
        this.emitters.add(emitter);
        // 연결이 완료되면 리스트에서 emitter를 제거하는 콜백을 설정합니다.
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        // 연결이 타임아웃되면 리스트에서 emitter를 제거하는 콜백을 설정합니다.
        emitter.onTimeout(() -> this.emitters.remove(emitter));
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
        // 전송에 실패한 emitter를 저장할 리스트입니다.
        List<SseEmitter> deadEmitters = new ArrayList<>();

        // 모든 emitter에 대해 이벤트를 전송합니다.
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(eventData));
            } catch (IOException e) {
                // 전송에 실패한 emitter를 deadEmitters 리스트에 추가합니다.
                deadEmitters.add(emitter);
            }
        });

        // 전송에 실패한 emitter들을 리스트에서 제거합니다.
        this.emitters.removeAll(deadEmitters);
    }

}
