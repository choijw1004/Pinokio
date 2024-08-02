package com.example.pinokkio.api.order;

import com.example.pinokkio.api.customer.CustomerService;
import com.example.pinokkio.api.pos.PosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Order Controller", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;
    private final PosService posService;
    private final CustomerService customerService;



}
