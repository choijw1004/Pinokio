package com.example.pinokkio.api.customer.dto.request;

import com.example.pinokkio.api.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationRequest {

    // 고객 정보를 담는 Customer 객체입니다.
    private Customer customer;

    // 얼굴 임베딩 데이터를 바이트 배열로 저장합니다.
    private byte[] faceEmbeddingData;


}
