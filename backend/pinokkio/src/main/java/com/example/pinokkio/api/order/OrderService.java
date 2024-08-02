package com.example.pinokkio.api.order;

import com.example.pinokkio.api.customer.Customer;
import com.example.pinokkio.api.customer.CustomerRepository;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.exception.domain.customer.CustomerNotFoundException;
import com.example.pinokkio.exception.domain.pos.PosNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final PosRepository posRepository;
    private final CustomerRepository customerRepository;

    /**
     * 포스 식별자, 고객 식별자, 주문 아이템 목록을 기반으로 주문 정보를 생성하여 반환한다.
     *
     * @param posId         포스 식별자
     * @param customerId    고객 식별자
     * @param orderItems    주문 아이템 목록
     * @return  생성된 주문 정보
     */
    public Order createOrder(UUID posId, UUID customerId, List<OrderItem> orderItems) {
        Pos pos = posRepository.
                findById(posId)
                .orElseThrow(() -> new PosNotFoundException(posId));
        //Pos 검증

        Customer customer = null;
        if(customerId != null) {
            customer = customerRepository
                    .findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException(customerId));
        }
        //Customer 검증

        Order order = Order
                .builder()
                .pos(pos)
                .customer(customer)
                .items(orderItems).build();
        //Order 생성
        return orderRepository.save(order);
    }

}
