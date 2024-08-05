package com.example.pinokkio.api.order;

import com.example.pinokkio.api.customer.Customer;
import com.example.pinokkio.api.customer.CustomerRepository;
import com.example.pinokkio.api.item.Item;
import com.example.pinokkio.api.item.ItemRepository;
import com.example.pinokkio.api.order.dto.request.GroupOrderItemRequest;
import com.example.pinokkio.api.order.dto.request.OrderItemRequest;
import com.example.pinokkio.api.order.orderitem.OrderItem;
import com.example.pinokkio.api.order.orderitem.OrderItemRepository;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.exception.domain.customer.CustomerNotFoundException;
import com.example.pinokkio.exception.domain.item.ItemAmountException;
import com.example.pinokkio.exception.domain.item.ItemNotFoundException;
import com.example.pinokkio.exception.domain.pos.PosNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    public Order createOrder(UUID posId, GroupOrderItemRequest dtoList) {

        // Pos 검증
        Pos pos = posRepository
                .findById(posId)
                .orElseThrow(() -> new PosNotFoundException(posId));

        UUID customerId = dtoList.getCustomerId() == null
                ? toUUID(pos.getDummyCustomerUUID())
                : toUUID(dtoList.getCustomerId());


        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // OrderItem 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest request : dtoList.getOrderItems()) {
            Item item = itemRepository
                    .findById(toUUID(request.getItemId()))
                    .orElseThrow(() -> new ItemNotFoundException(toUUID(request.getItemId())));

            // Item 수량 체크
            if (item.getAmount() < request.getQuantity()) {
                throw new ItemAmountException(item.getId());
            }

            // Item 수량 차감
            item.updateAmount(-request.getQuantity());

            // OrderItem 생성
            OrderItem orderItem = new OrderItem(null, item, request.getQuantity());
            orderItems.add(orderItem);
        }

        // Order 생성
        Order order = Order.builder()
                .pos(pos)
                .customer(customer)
                .items(orderItems)
                .build();

        // OrderItem 의 Order 설정 및 저장
        orderItems.forEach(orderItem -> {
            orderItem.updateOrder(order);
            orderItemRepository.save(orderItem);
        });

        // Order 저장
        return orderRepository.save(order);
    }


    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }
}
