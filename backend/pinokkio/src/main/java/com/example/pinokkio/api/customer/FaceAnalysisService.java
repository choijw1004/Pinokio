package com.example.pinokkio.api.customer;


import com.example.pinokkio.api.customer.dto.response.AnalysisResult;
import com.example.pinokkio.api.customer.sse.SSEService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FaceAnalysisService {

    @Value("${fastapi.url}")
    private String fastApiUrl;

    @Value("${fastapi.timeout}")
    private int timeout;

    private final RestTemplate restTemplate;
    private final SSEService sseService;
    private final CustomerService customerService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 이미지 리스트를 반환한 결
     * @param images 이미지 리스트
     * @return 이미지 리스트를 분석한 결과를
     */
    public AnalysisResult analyzeImages(List<String> images) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("images", images);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            /**
             * FastAPI 서버로 POST 요청을 보내고 응답을 받는다.
             */
            ResponseEntity<AnalysisResult> response = restTemplate.exchange(
                    fastApiUrl + "/analyze_faces",
                    HttpMethod.POST,
                    request,
                    AnalysisResult.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                AnalysisResult result = response.getBody();
                log.info("Face analysis completed successfully");

                if (result != null && result.isFace()) {
                    // 얼굴이 감지된 경우, Redis에 결과를 캐싱
                    cacheAnalysisResult(result);
                    // CustomerService를 호출하여 고객 조회
                    customerService.findOrRegisterCustomer(result);
                } else {
                    sseService.sendWaitingEvent(false);
                    log.info("No face detected, sending stop waiting event");
                }
                return result;
            } else {
                log.error("Face analysis failed with status code: {}", response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            log.error("Error during face analysis", e);
            return null;
        }
    }

    /**
     * 분석 결과를 Redis 에 캐싱하는 메소드
     * @param result 분석 결과
     */
    private void cacheAnalysisResult(AnalysisResult result) {
        String cacheKey = "analysis_result:" + result.getEncryptedEmbedding();
        redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.MINUTES);
    }

}
