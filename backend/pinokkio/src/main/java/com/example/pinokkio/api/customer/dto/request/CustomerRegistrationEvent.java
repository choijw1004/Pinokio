package com.example.pinokkio.api.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerRegistrationEvent {
    private final CustomerRegistrationRequest request;
}
