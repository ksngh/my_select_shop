package kr.myselectshop.util;

import kr.myselectshop.entity.Product;
import kr.myselectshop.entity.User;
import kr.myselectshop.entity.UserRoleEnum;
import kr.myselectshop.naver.dto.ItemDto;
import kr.myselectshop.naver.service.NaverApiService;
import kr.myselectshop.repository.ProductRepository;
import kr.myselectshop.repository.UserRepository;
import kr.myselectshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static kr.myselectshop.service.ProductService.MIN_MY_PRICE;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements ApplicationRunner {


    private final UserService userService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NaverApiService naverApiService;

    @Override
    public void run(ApplicationArguments args) {
        // 테스트 User 생성
        User testUser = new User("Robbie", passwordEncoder.encode("1234"), "robbie@sparta.com", UserRoleEnum.USER);
        testUser = userRepository.save(testUser);

        // 테스트 User 의 관심상품 등록
        // 검색어 당 관심상품 10개 등록
        createTestData(testUser, "신발");
        createTestData(testUser, "과자");
        createTestData(testUser, "키보드");
        createTestData(testUser, "휴지");
        createTestData(testUser, "휴대폰");
        createTestData(testUser, "앨범");
        createTestData(testUser, "헤드폰");
        createTestData(testUser, "이어폰");
        createTestData(testUser, "노트북");
        createTestData(testUser, "무선 이어폰");
        createTestData(testUser, "모니터");
    }

    private void createTestData(User user, String searchWord) {
        // 네이버 쇼핑 API 통해 상품 검색
        List<ItemDto> itemDtoList = naverApiService.searchItems(searchWord);

        List<Product> productList = new ArrayList<>();

        for (ItemDto itemDto : itemDtoList) {
            Product product = new Product();
            // 관심상품 저장 사용자
            product.setUser(user);
            // 관심상품 정보
            product.setTitle(itemDto.getTitle());
            product.setLink(itemDto.getLink());
            product.setImage(itemDto.getImage());
            product.setLprice(itemDto.getLprice());

            // 희망 최저가 랜덤값 생성
            // 최저 (100원) ~ 최대 (상품의 현재 최저가 + 10000원)
            int myPrice = getRandomNumber(MIN_MY_PRICE, itemDto.getLprice() + 10000);
            product.setMyprice(myPrice);

            productList.add(product);
        }

        productRepository.saveAll(productList);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}