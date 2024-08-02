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
import com.example.pinokkio.exception.domain.item.ItemNotFoundException;
import com.example.pinokkio.exception.domain.pos.PosNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    /**
     * 컨트롤러에서 GroupOrderItemRequest 받아오기
     * -> OrderItem 으로 변형시켜서 저장하기
     * -> Order 정보 저장하기
     * 
     * 도중에 수량 카운트 검증 필요
     * 
     */
    
    
    public Order createOrder(UUID posId, UUID customerId, GroupOrderItemRequest dtoList) {

        //Pos 검증
        Pos pos = posRepository.findById(posId)
                .orElseThrow(() -> new PosNotFoundException(posId));

        // OrderItem 리스트 생성
        List<OrderItem> orderItems = dtoList.getOrderItems().stream()
                .map(request -> {
                    Item item = itemRepository
                            .findById(toUUID(request.getItemId()))
                            .orElseThrow(() -> new ItemNotFoundException(toUUID(request.getItemId())));
                    return new OrderItem(null, item, request.getQuantity());
                })
                .toList();


        Customer customer = null;
        //CASE 1:고객
        if (customerId != null) {
            customer = customerRepository
                    .findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException(customerId));
        }
        //CASE 2:비고객
        else {
            customer = customerRepository
                    .findById(toUUID(pos.getDummyCustomerUUID()))
                    .orElseThrow(()-> new CustomerNotFoundException(toUUID(pos.getDummyCustomerUUID())));
        }

        // Order 생성
        Order order = Order.builder()
                .pos(pos)
                .customer(customer)
                .items(orderItems)
                .build();

        // OrderItem의 Order 설정
        orderItems.forEach(orderItem -> {
            orderItem.updateOrder(order);
            orderItemRepository.save(orderItem);
            // 각 OrderItem을 저장
        });

        return orderRepository.save(order);
    }

    /**
     * String to UUID
     */
    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }
}
