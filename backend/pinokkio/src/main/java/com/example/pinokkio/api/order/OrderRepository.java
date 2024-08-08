package com.example.pinokkio.api.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * 특정 고객이 최근 주문한 순서로 Order 리스트를 반환한다.
     * @param customerId 고객 식별자
     * @return 특정 고객이 최근 주문한 Order 리스트
     */
    List<Order> findByCustomerIdOrderByCreatedDateDesc(UUID customerId);

    List<Order> findAllByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
