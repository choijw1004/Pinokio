package com.example.pinokkio.common;

import com.example.pinokkio.api.category.Category;
import com.example.pinokkio.api.category.CategoryRepository;
import com.example.pinokkio.api.customer.Customer;
import com.example.pinokkio.api.customer.CustomerRepository;
import com.example.pinokkio.api.item.Item;
import com.example.pinokkio.api.item.ItemRepository;
import com.example.pinokkio.api.kiosk.Kiosk;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.order.Order;
import com.example.pinokkio.api.order.OrderRepository;
import com.example.pinokkio.api.order.orderitem.OrderItem;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.api.pos.code.Code;
import com.example.pinokkio.api.pos.code.CodeRepository;
import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.api.teller.TellerRepository;
import com.example.pinokkio.common.type.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    private static final Map<String, String> POS_NAME_TO_EMAIL_DOMAIN = Map.of(
            "스타벅스", "starbucks",
            "탐앤탐스", "tomntoms",
            "할리스", "hollys"
    );

    private final CodeRepository codeRepository;
    private final CategoryRepository categoryRepository;
    private final PosRepository posRepository;
    private final KioskRepository kioskRepository;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        makeInitData();
//        createDummyOrders();
    }

    private void makeInitData() {
        // Code Repository 데이터 추가
        Code starbucksCode = codeRepository.save(new Code("스타벅스"));
        Code tomntomsCoffeeCode = codeRepository.save(new Code("탐앤탐스"));
        Code hollysCode = codeRepository.save(new Code("할리스"));

        // Pos Repository 데이터 추가 + 비고객 생성
        Pos starbucks = posRepository.save(Pos.builder()
                .code(starbucksCode)
                .email("starbucks@example.com")
                .password(passwordEncoder.encode("1234"))
                .build());
        Customer starbucksCustomer = customerRepository.save(Customer.builder()
                .phoneNumber("00000000")
                .pos(starbucks)
                .age(99)
                .gender(Gender.MALE)
                .faceEmbedding(null)
                .build());
        starbucks.updateDummyCustomerUUID(starbucksCustomer.getId());


        Pos tomntomsCoffee = posRepository.save(Pos.builder()
                .code(tomntomsCoffeeCode)
                .email("tomntoms@example.com")
                .password(passwordEncoder.encode("1234"))
                .build());
        Customer tomntomsCustomer = customerRepository.save(Customer.builder()
                .phoneNumber("00000000")
                .pos(tomntomsCoffee)
                .age(99)
                .gender(Gender.MALE)
                .faceEmbedding(null)
                .build());
        tomntomsCoffee.updateDummyCustomerUUID(tomntomsCustomer.getId());


        Pos hollys = posRepository.save(Pos.builder()
                .code(hollysCode)
                .email("hollys@example.com")
                .password(passwordEncoder.encode("1234"))
                .build());
        Customer hollysCustomer = customerRepository.save(Customer.builder()
                .phoneNumber("00000000")
                .pos(hollys)
                .age(99)
                .gender(Gender.MALE)
                .faceEmbedding(null)
                .build());
        hollys.updateDummyCustomerUUID(hollysCustomer.getId());


        // Category Repository 데이터 추가
        Category coffeeStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("커피")
                .build());

        Category decafStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("디카페인")
                .build());

        Category smoothieStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("스무디")
                .build());

        Category teaAideStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("티/에이드")
                .build());

        Category bakeryStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("베이커리")
                .build());

        Category mdStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("MD")
                .build());

        // 탐앤탐스 카테고리
        Category coffeeTomntoms = categoryRepository.save(Category.builder()
                .pos(tomntomsCoffee)
                .name("커피")
                .build());

        Category decafTomntoms = categoryRepository.save(Category.builder()
                .pos(tomntomsCoffee)
                .name("디카페인")
                .build());

        Category smoothieTomntoms = categoryRepository.save(Category.builder()
                .pos(tomntomsCoffee)
                .name("스무디")
                .build());

        Category teaAideTomntoms = categoryRepository.save(Category.builder()
                .pos(tomntomsCoffee)
                .name("티/에이드")
                .build());

        Category bakeryTomntoms = categoryRepository.save(Category.builder()
                .pos(tomntomsCoffee)
                .name("베이커리")
                .build());

        Category mdTomntoms = categoryRepository.save(Category.builder()
                .pos(tomntomsCoffee)
                .name("MD")
                .build());

        // 할리스 카테고리
        Category coffeeHollys = categoryRepository.save(Category.builder()
                .pos(hollys)
                .name("커피")
                .build());

        Category decafHollys = categoryRepository.save(Category.builder()
                .pos(hollys)
                .name("디카페인")
                .build());

        Category smoothieHollys = categoryRepository.save(Category.builder()
                .pos(hollys)
                .name("스무디")
                .build());

        Category teaAideHollys = categoryRepository.save(Category.builder()
                .pos(hollys)
                .name("티/에이드")
                .build());

        Category bakeryHollys = categoryRepository.save(Category.builder()
                .pos(hollys)
                .name("베이커리")
                .build());

        Category mdHollys = categoryRepository.save(Category.builder()
                .pos(hollys)
                .name("MD")
                .build());

        // Kiosk Repository 데이터 추가
        addKiosks(starbucks);
        addKiosks(tomntomsCoffee);
        addKiosks(hollys);

        // Teller Repository 데이터 추가
        addTellers(starbucks);
        addTellers(tomntomsCoffee);
        addTellers(hollys);

        // Item Repository 데이터 추가
        addItems(starbucks, coffeeStarbucks, decafStarbucks, smoothieStarbucks, teaAideStarbucks, bakeryStarbucks, mdStarbucks);
        addItems(tomntomsCoffee, coffeeTomntoms, decafTomntoms, smoothieTomntoms, teaAideTomntoms, bakeryTomntoms, mdTomntoms);
        addItems(hollys, coffeeHollys, decafHollys, smoothieHollys, teaAideHollys, bakeryHollys, mdHollys);
    }

    private void addKiosks(Pos pos) {
        String domain = POS_NAME_TO_EMAIL_DOMAIN.get(pos.getCode().getName());
        for (int i = 1; i <= 3; i++) {
            String email = "kiosk" + i + "@" + domain + ".com";
            kioskRepository.save(new Kiosk(pos, email, passwordEncoder.encode("1234")));
        }
    }

    private void addTellers(Pos pos) {
        String domain = POS_NAME_TO_EMAIL_DOMAIN.get(pos.getCode().getName());
        for (int i = 1; i <= 4; i++) {
            String email = "teller" + i + "@" + domain + ".com";
            tellerRepository.save(new Teller(pos.getCode(), email, passwordEncoder.encode("1234")));
        }
    }

    private void addItems(Pos pos, Category... categories) {
        for (Category category : categories) {
            switch (category.getName()) {
                case "커피":
                    itemRepository.save(new Item(pos, category, 4500, 100, "아메리카노", "깊고 진한 에스프레소와 뜨거운 물을 섞은 커피", "아메리카노 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5000, 100, "카페라떼", "에스프레소와 스팀 밀크를 섞은 부드러운 커피", "카페라떼 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5500, 100, "카푸치노", "에스프레소에 스팀 밀크와 거품을 얹은 커피", "카푸치노 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5500, 100, "바닐라 라떼", "바닐라 시럽이 들어간 카페라떼", "바닐라 라떼 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "카라멜 마키아토", "바닐라 시럽과 카라멜 드리즐이 들어간 카페라떼", "카라멜 마키아토 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5000, 100, "에스프레소", "진한 에스프레소 샷", "에스프레소 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "모카", "초콜릿 시럽이 들어간 카페라떼", "모카 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "콜드브루", "차갑게 우려낸 깔끔한 커피", "콜드브루 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7000, 100, "니트로 콜드브루", "질소가 주입된 부드러운 콜드브루", "니트로 콜드브루 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "플랫화이트", "진한 에스프레소와 스팀 밀크를 섞은 커피", "플랫화이트 이미지 URL"));
                    break;
                case "디카페인":
                    itemRepository.save(new Item(pos, category, 5000, 100, "디카페인 아메리카노", "카페인을 제거한 에스프레소로 만든 아메리카노", "디카페인 아메리카노 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5500, 100, "디카페인 카페라떼", "카페인을 제거한 에스프레소와 스팀 밀크를 섞은 라떼", "디카페인 카페라떼 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "디카페인 카푸치노", "카페인을 제거한 에스프레소에 스팀 밀크와 거품을 얹은 커피", "디카페인 카푸치노 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "디카페인 바닐라 라떼", "카페인을 제거한 에스프레소로 만든 바닐라 라떼", "디카페인 바닐라 라떼 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "디카페인 카라멜 마키아토", "카페인을 제거한 에스프레소로 만든 카라멜 마키아토", "디카페인 카라멜 마키아토 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5500, 100, "디카페인 에스프레소", "카페인을 제거한 진한 에스프레소 샷", "디카페인 에스프레소 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "디카페인 모카", "카페인을 제거한 에스프레소로 만든 모카", "디카페인 모카 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7000, 100, "디카페인 콜드브루", "카페인을 제거한 원두로 만든 콜드브루", "디카페인 콜드브루 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7500, 100, "디카페인 니트로 콜드브루", "카페인을 제거한 원두로 만든 니트로 콜드브루", "디카페인 니트로 콜드브루 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "디카페인 플랫화이트", "카페인을 제거한 에스프레소로 만든 플랫화이트", "디카페인 플랫화이트 이미지 URL"));
                    break;
                case "스무디":
                    itemRepository.save(new Item(pos, category, 6000, 100, "딸기 스무디", "신선한 딸기로 만든 스무디", "딸기 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "망고 스무디", "달콤한 망고로 만든 스무디", "망고 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "블루베리 스무디", "새콤달콤한 블루베리로 만든 스무디", "블루베리 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "요구르트 스무디", "부드러운 요구르트로 만든 스무디", "요구르트 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "키위 스무디", "상큼한 키위로 만든 스무디", "키위 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "패션후르츠 스무디", "이국적인 패션후르츠로 만든 스무디", "패션후르츠 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7000, 100, "그린 스무디", "신선한 채소와 과일로 만든 건강 스무디", "그린 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7000, 100, "초코 바나나 스무디", "초콜릿과 바나나가 어우러진 스무디", "초코 바나나 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7000, 100, "복숭아 스무디", "달콤한 복숭아로 만든 스무디", "복숭아 스무디 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 7500, 100, "베리 믹스 스무디", "다양한 베리가 섞인 스무디", "베리 믹스 스무디 이미지 URL"));
                    break;
                case "티/에이드":
                    itemRepository.save(new Item(pos, category, 5000, 100, "얼그레이 티", "향긋한 얼그레이 티", "얼그레이 티 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5000, 100, "페퍼민트 티", "상쾌한 페퍼민트 티", "페퍼민트 티 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5000, 100, "캐모마일 티", "은은한 캐모마일 티", "캐모마일 티 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5500, 100, "잉글리시 브렉퍼스트 티", "진한 맛의 잉글리시 브렉퍼스트 티", "잉글리시 브렉퍼스트 티 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5500, 100, "자스민 티", "향긋한 자스민 티", "자스민 티 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "레몬에이드", "상큼한 레몬에이드", "레몬에이드 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "자몽에이드", "새콤달콤한 자몽에이드", "자몽에이드 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6000, 100, "청포도에이드", "달콤한 청포도에이드", "청포도에이드 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "패션후르츠에이드", "이국적인 패션후르츠에이드", "패션후르츠에이드 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 6500, 100, "블루베리에이드", "새콤달콤한 블루베리에이드", "블루베리에이드 이미지 URL"));
                    break;
                case "베이커리":
                    itemRepository.save(new Item(pos, category, 3500, 100, "크로아상", "바삭한 크로아상", "크로아상 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4000, 100, "초코 머핀", "초콜릿이 가득한 머핀", "초코 머핀 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4000, 100, "블루베리 머핀", "블루베리가 가득한 머핀", "블루베리 머핀 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4500, 100, "치즈케이크", "부드러운 치즈케이크", "치즈케이크 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4500, 100, "티라미수", "커피 향이 가득한 티라미수", "티라미수 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3000, 100, "플레인 베이글", "쫄깃한 플레인 베이글", "플레인 베이글 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3500, 100, "크림치즈 베이글", "크림치즈를 곁들인 베이글", "크림치즈 베이글 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 5000, 100, "레드벨벳 케이크", "달콤한 레드벨벳 케이크", "레드벨벳 케이크 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3500, 100, "스콘", "버터 향이 가득한 스콘", "스콘 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4000, 100, "당근 케이크", "건강한 당근 케이크", "당근 케이크 이미지 URL"));
                    break;
                case "MD":
                    itemRepository.save(new Item(pos, category, 15000, 50, "텀블러", "스테인리스 스틸 텀블러", "텀블러 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 20000, 50, "머그컵", "도자기 머그컵", "머그컵 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 25000, 30, "보온병", "스테인리스 스틸 보온병", "보온병 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 10000, 100, "에코백", "친환경 에코백", "에코백 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 8000, 100, "키체인", "귀여운 키체인", "키체인 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 30000, 20, "플레이트", "도자기 플레이트", "플레이트 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 12000, 50, "티 스푼", "스테인리스 스틸 티 스푼", "티 스푼 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 35000, 30, "프렌치프레스", "유리 프렌치프레스", "프렌치프레스 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 18000, 50, "물병", "유리 물병", "물병 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 22000, 40, "커피 저장 용기", "밀폐형 커피 저장 용기", "커피 저장 용기 이미지 URL"));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + category.getName());
            }
        }
    }

    private void createDummyOrders() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.now();

        List<Pos> allPos = posRepository.findAll();
        List<Customer> allCustomers = customerRepository.findAll();
        List<Item> allItems = itemRepository.findAll();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int ordersPerDay = 10 + random.nextInt(11); // 10 to 20 orders per day
            for (int i = 0; i < ordersPerDay; i++) {
                Pos pos = allPos.get(random.nextInt(allPos.size()));
                Customer customer = allCustomers.get(random.nextInt(allCustomers.size()));

                List<OrderItem> orderItems = createRandomOrderItems(pos, customer.getId(), allItems);
                long totalPrice = orderItems.stream()
                        .mapToLong(item -> item.getItem().getPrice() * item.getQuantity())
                        .sum();

                Order order = Order.builder()
                        .pos(pos)
                        .customer(customer)
                        .items(orderItems)
                        .totalPrice(totalPrice)
                        .build();

                // Set random time for the order
                LocalDateTime orderDateTime = date.atTime(8 + random.nextInt(14), random.nextInt(60));
//                order.setCreatedDate(orderDateTime);

                orderRepository.save(order);

                // Update order for each OrderItem
                for (OrderItem item : orderItems) {
                    item.updateOrder(order);
                }
            }
        }
    }

    private List<OrderItem> createRandomOrderItems(Pos pos, UUID customerId, List<Item> allItems) {
        int itemCount = 1 + random.nextInt(5); // 1 to 5 items per order
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            Item item = allItems.stream()
                    .filter(it -> it.getPos().equals(pos))
                    .skip(random.nextInt((int) allItems.stream().filter(it -> it.getPos().equals(pos)).count()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No items found for POS"));

            int quantity = 1 + random.nextInt(3); // 1 to 3 quantity for each item
            orderItems.add(new OrderItem(null, item, customerId, quantity));
        }

        return orderItems;
    }

}