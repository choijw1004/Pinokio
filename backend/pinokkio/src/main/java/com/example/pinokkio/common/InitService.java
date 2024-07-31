package com.example.pinokkio.common;

import com.example.pinokkio.api.category.Category;
import com.example.pinokkio.api.category.CategoryRepository;
import com.example.pinokkio.api.item.Item;
import com.example.pinokkio.api.item.ItemRepository;
import com.example.pinokkio.api.kiosk.Kiosk;
import com.example.pinokkio.api.kiosk.KioskRepository;
import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.api.pos.PosRepository;
import com.example.pinokkio.api.pos.code.Code;
import com.example.pinokkio.api.pos.code.CodeRepository;
import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.api.teller.TellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Map<String, String> POS_NAME_TO_EMAIL_DOMAIN = Map.of(
            "맥도날드", "mcdonalds",
            "롯데리아", "lotteria",
            "KFC", "kfc",
            "맘스터치", "momstouch",
            "버거킹", "burgerking",
            "스타벅스", "starbucks",
            "이디야", "ediya",
            "파리바게뜨", "parisbaguette",
            "던킨", "dunkin"
    );

    private final CodeRepository codeRepository;
    private final CategoryRepository categoryRepository;
    private final PosRepository posRepository;
    private final KioskRepository kioskRepository;
    private final TellerRepository tellerRepository;
    private final ItemRepository itemRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Code Repository 데이터 추가
        Code mcdonaldsCode = codeRepository.save(new Code("맥도날드"));
        Code lotteriaCode = codeRepository.save(new Code("롯데리아"));
        Code kfcCode = codeRepository.save(new Code("KFC"));
        Code momstouchCode = codeRepository.save(new Code("맘스터치"));
        Code burgerkingCode = codeRepository.save(new Code("버거킹"));
        Code starbucksCode = codeRepository.save(new Code("스타벅스"));
        Code ediyaCode = codeRepository.save(new Code("이디야"));
        Code parisBaguetteCode = codeRepository.save(new Code("파리바게뜨"));
        Code dunkinCode = codeRepository.save(new Code("던킨"));

        // Pos Repository 데이터 추가
        Pos mcdonalds = posRepository.save(Pos.builder()
                .code(mcdonaldsCode)
                .email("mcdonalds@example.com")
                .password("맥도날드")
                .build());

        Pos lotteria = posRepository.save(Pos.builder()
                .code(lotteriaCode)
                .email("lotteria@example.com")
                .password("롯데리아")
                .build());

        Pos kfc = posRepository.save(Pos.builder()
                .code(kfcCode)
                .email("kfc@example.com")
                .password("KFC")
                .build());

        Pos momstouch = posRepository.save(Pos.builder()
                .code(momstouchCode)
                .email("momstouch@example.com")
                .password("맘스터치")
                .build());

        Pos burgerking = posRepository.save(Pos.builder()
                .code(burgerkingCode)
                .email("burgerking@example.com")
                .password("버거킹")
                .build());

        Pos starbucks = posRepository.save(Pos.builder()
                .code(starbucksCode)
                .email("starbucks@example.com")
                .password("스타벅스")
                .build());

        Pos ediya = posRepository.save(Pos.builder()
                .code(ediyaCode)
                .email("ediya@example.com")
                .password("이디야")
                .build());

        Pos parisBaguette = posRepository.save(Pos.builder()
                .code(parisBaguetteCode)
                .email("parisbaguette@example.com")
                .password("파리바게뜨")
                .build());

        Pos dunkin = posRepository.save(Pos.builder()
                .code(dunkinCode)
                .email("dunkin@example.com")
                .password("던킨")
                .build());

        // Category Repository 데이터 추가
        Category drinksMcdonalds = categoryRepository.save(Category.builder()
                .pos(mcdonalds)
                .name("음료")
                .build());

        Category snacksMcdonalds = categoryRepository.save(Category.builder()
                .pos(mcdonalds)
                .name("간식")
                .build());

        Category burgersMcdonalds = categoryRepository.save(Category.builder()
                .pos(mcdonalds)
                .name("버거")
                .build());

        Category dessertsMcdonalds = categoryRepository.save(Category.builder()
                .pos(mcdonalds)
                .name("디저트")
                .build());

        Category snacksLotteria = categoryRepository.save(Category.builder()
                .pos(lotteria)
                .name("간식")
                .build());

        Category burgersKfc = categoryRepository.save(Category.builder()
                .pos(kfc)
                .name("버거")
                .build());

        Category snacksKfc = categoryRepository.save(Category.builder()
                .pos(kfc)
                .name("간식")
                .build());

        Category dessertsMomstouch = categoryRepository.save(Category.builder()
                .pos(momstouch)
                .name("디저트")
                .build());

        Category burgersBurgerking = categoryRepository.save(Category.builder()
                .pos(burgerking)
                .name("버거")
                .build());

        Category coffeeStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("커피")
                .build());

        Category dessertsStarbucks = categoryRepository.save(Category.builder()
                .pos(starbucks)
                .name("디저트")
                .build());

        Category coffeeEdiya = categoryRepository.save(Category.builder()
                .pos(ediya)
                .name("커피")
                .build());

        Category dessertsEdiya = categoryRepository.save(Category.builder()
                .pos(ediya)
                .name("디저트")
                .build());

        Category bakeryParisBaguette = categoryRepository.save(Category.builder()
                .pos(parisBaguette)
                .name("베이커리")
                .build());

        Category dessertsParisBaguette = categoryRepository.save(Category.builder()
                .pos(parisBaguette)
                .name("디저트")
                .build());

        Category snacksDunkin = categoryRepository.save(Category.builder()
                .pos(dunkin)
                .name("간식")
                .build());

        Category coffeeDunkin = categoryRepository.save(Category.builder()
                .pos(dunkin)
                .name("커피")
                .build());

        // Kiosk Repository 데이터 추가
        addKiosks(mcdonalds);
        addKiosks(lotteria);
        addKiosks(kfc);
        addKiosks(momstouch);
        addKiosks(burgerking);
        addKiosks(starbucks);
        addKiosks(ediya);
        addKiosks(parisBaguette);
        addKiosks(dunkin);

        // Teller Repository 데이터 추가
        addTellers(mcdonalds);
        addTellers(lotteria);
        addTellers(kfc);
        addTellers(momstouch);
        addTellers(burgerking);
        addTellers(starbucks);
        addTellers(ediya);
        addTellers(parisBaguette);
        addTellers(dunkin);

        // Item Repository 데이터 추가
        addItems(mcdonalds, drinksMcdonalds, snacksMcdonalds, burgersMcdonalds, dessertsMcdonalds);
        addItems(lotteria, snacksLotteria);
        addItems(kfc, burgersKfc, snacksKfc);
        addItems(momstouch, dessertsMomstouch);
        addItems(burgerking, burgersBurgerking);
        addItems(starbucks, coffeeStarbucks, dessertsStarbucks);
        addItems(ediya, coffeeEdiya, dessertsEdiya);
        addItems(parisBaguette, bakeryParisBaguette, dessertsParisBaguette);
        addItems(dunkin, snacksDunkin, coffeeDunkin);
    }

    private void addKiosks(Pos pos) {
        String domain = POS_NAME_TO_EMAIL_DOMAIN.get(pos.getCode().getName());
        for (int i = 1; i <= 3; i++) {
            String email = "kiosk" + i + "@" + domain + ".com";
            kioskRepository.save(new Kiosk(pos, email, pos.getPassword()));
        }
    }

    private void addTellers(Pos pos) {
        String domain = POS_NAME_TO_EMAIL_DOMAIN.get(pos.getCode().getName());
        for (int i = 1; i <= 4; i++) {
            String email = "teller" + i + "@" + domain + ".com";
            tellerRepository.save(new Teller(pos.getCode(), email, pos.getPassword(), 10000));
        }
    }

    private void addItems(Pos pos, Category... categories) {
        for (Category category : categories) {
            switch (pos.getCode().getName()) {
                case "맥도날드":
                    itemRepository.save(new Item(pos, category, 4500, 100, "빅맥", "소고기 패티 2장, 치즈, 양상추, 피클, 양파, 특제 소스가 들어간 버거", "빅맥 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3000, 100, "맥너겟", "튀긴 치킨 너겟", "맥너겟 이미지 URL"));
                    break;
                case "롯데리아":
                    itemRepository.save(new Item(pos, category, 5000, 100, "불고기버거", "불고기 패티와 양상추, 마요네즈 소스가 들어간 버거", "불고기버거 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3500, 100, "너겟", "튀긴 치킨 너겟", "너겟 이미지 URL"));
                    break;
                case "KFC":
                    itemRepository.save(new Item(pos, category, 6000, 100, "핫윙", "매운 양념이 묻은 윙", "핫윙 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3500, 100, "치킨너겟", "KFC의 치킨너겟", "치킨너겟 이미지 URL"));
                    break;
                case "맘스터치":
                    itemRepository.save(new Item(pos, category, 7000, 100, "싸이순살", "매운 양념이 묻은 치킨", "싸이순살 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4000, 100, "해바라기 씨앗", "스낵", "해바라기 씨앗 이미지 URL"));
                    break;
                case "버거킹":
                    itemRepository.save(new Item(pos, category, 5500, 100, "와퍼", "치즈와 소고기 패티가 들어간 버거", "와퍼 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3200, 100, "너겟", "버거킹의 치킨 너겟", "너겟 이미지 URL"));
                    break;
                case "스타벅스":
                    itemRepository.save(new Item(pos, category, 5500, 100, "카라멜 마끼아또", "카라멜 소스가 들어간 마끼아또", "카라멜 마끼아또 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4500, 100, "치즈 케이크", "부드러운 치즈 케이크", "치즈 케이크 이미지 URL"));
                    break;
                case "이디야":
                    itemRepository.save(new Item(pos, category, 5000, 100, "아메리카노", "쓴 맛이 특징인 아메리카노", "아메리카노 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4000, 100, "프리마", "디저트 쿠키", "프리마 이미지 URL"));
                    break;
                case "파리바게뜨":
                    itemRepository.save(new Item(pos, category, 6000, 100, "생크림 케이크", "부드러운 생크림 케이크", "생크림 케이크 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 3500, 100, "크로와상", "버터가 듬뿍 들어간 크로와상", "크로와상 이미지 URL"));
                    break;
                case "던킨":
                    itemRepository.save(new Item(pos, category, 5000, 100, "도넛", "달콤한 도넛", "도넛 이미지 URL"));
                    itemRepository.save(new Item(pos, category, 4000, 100, "커피", "부드러운 커피", "커피 이미지 URL"));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + pos.getCode().getName());
            }
        }
    }
}
