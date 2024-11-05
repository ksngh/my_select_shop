package kr.myselectshop.service;

import jakarta.transaction.Transactional;
import kr.myselectshop.dto.ProductMypriceRequestDto;
import kr.myselectshop.dto.ProductRequestDto;
import kr.myselectshop.dto.ProductResponseDto;
import kr.myselectshop.entity.Product;
import kr.myselectshop.entity.User;
import kr.myselectshop.entity.UserRoleEnum;
import kr.myselectshop.naver.dto.ItemDto;
import kr.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user) {
        Product product = productRepository.save(new Product(productRequestDto, user));
        return new ProductResponseDto(product);
    }

    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto productMypriceRequestDto) {
        int myPrice = productMypriceRequestDto.getMyprice();
        if (myPrice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해주세요.");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품을 찾을 수 없습니다."));

        product.update(productMypriceRequestDto);
        return null;
    }

    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRole = user.getRole();
        Page<Product> products;

        if (userRole == UserRoleEnum.USER) {
            products = productRepository.findAllByUser(user, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        return products.map(ProductResponseDto::new);
    }

    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품은 존재하지 않습니다."));
        product.updateByItemDto(itemDto);
    }

}
