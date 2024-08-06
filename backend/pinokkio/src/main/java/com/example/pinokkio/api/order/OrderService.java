package com.example.pinokkio.api.order;

import com.example.pinokkio.api.customer.Customer;
import com.example.pinokkio.api.customer.CustomerRepository;
import com.example.pinokkio.api.item.Item;
import com.example.pinokkio.api.item.ItemRepository;
import com.example.pinokkio.api.order.dto.request.GroupOrderItemRequest;
import com.example.pinokkio.api.order.dto.request.OrderItemRequest;
import com.example.pinokkio.api.order.dto.response.TopOrderedItemResponse;
import com.example.pinokkio.api.order.orderitem.OrderItem;
import com.example.pinokkio.api.order.orderitem.OrderItemRepository;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.exception.domain.customer.CustomerNotFoundException;
import com.example.pinokkio.exception.domain.customer.NotCustomerOfPosException;
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
import java.util.Optional;
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
     * 주문 요청정보를 기반으로 주문을 생성한다.
     * @param posId     포스 식별자
     * @param dtoList   주문 요청정보
     * @return 생성된 주문 정보
     */
    public Order createOrder(UUID posId, GroupOrderItemRequest dtoList) {

        // Pos 검증
        Pos pos = posRepository
                .findById(posId)
                .orElseThrow(() -> new PosNotFoundException(posId));

        UUID customerId = dtoList.getCustomerId() == null
                ? toUUID(pos.getDummyCustomerUUID())
                : toUUID(dtoList.getCustomerId());

        validateCustomer(customerId, posId);

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

            // Item 수량 차감 + OrderItem 생성
            item.updateAmount(-request.getQuantity());
            OrderItem orderItem = new OrderItem(null, item, customerId, request.getQuantity());
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

    /**
     * 고객 식별자를 기반으로 가장 많이 주문한 아이템 정보를 반환한다.
     * @param customerId 고객 식별자
     * @return 가장 많이 주문된 아이템 정보를 담은 TopOrderedItemResponse
     */
    public Optional<TopOrderedItemResponse> getTopOrderedItemByCustomerId(UUID customerId, UUID posId) {
        //검증
        validateCustomer(customerId, posId);
        //리스트 생성
        List<Object[]> results = orderItemRepository.findTopOrderedItemsByCustomerId(customerId);
        if (!results.isEmpty()) {
            Object[] topItem = results.getFirst();
            Item item = (Item) topItem[0];
            int totalQuantity = ((Number) topItem[1]).intValue();
            return Optional.of(new TopOrderedItemResponse(
                    item.getId().toString(),
                    item.getName(),
                    totalQuantity)
            );
        }
        //주문한 아이템이 없을 경우
        return Optional.empty();
    }

    /**
     * 입력받은 고객이 입력받은 포스의 고객이 맞는지 확인한다.
     * @param customerId 고객 식별자
     * @param posId      포스 식별자
     */
    public void validateCustomer(UUID customerId, UUID posId) {
        Pos pos = posRepository
                .findById(posId)
                .orElseThrow(() -> new PosNotFoundException(posId));
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        if (!customer.getId().equals(toUUID(pos.getDummyCustomerUUID())))
            throw new NotCustomerOfPosException(customerId);
    }

    /**
     * 특정 고객의 최근 주문에서 아이템 리스트를 반환한다.
     * @param customerId 고객 식별자
     * @param posId      포스 식별자
     * @return 최근 주문의 아이템 리스트
     */
    public List<OrderItem> getRecentOrderItemsByCustomerId(UUID customerId, UUID posId) {
        validateCustomer(customerId, posId);
        return orderRepository.findByCustomerIdOrderByCreatedDateDesc(customerId)
                .stream()
                .findFirst()
                .map(Order::getItems)
                .orElseGet(ArrayList::new);
    }


    public UUID toUUID(String input) {
        return UUID.fromString(input);
    }
}
