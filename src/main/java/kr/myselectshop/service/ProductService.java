package kr.myselectshop.service;

import jakarta.transaction.Transactional;
import kr.myselectshop.dto.ProductMypriceRequestDto;
import kr.myselectshop.entity.Product;
import kr.myselectshop.dto.ProductRequestDto;
import kr.myselectshop.dto.ProductResponseDto;
import kr.myselectshop.naver.dto.ItemDto;
import kr.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productRepository.save(new Product(productRequestDto));
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

    public List<ProductResponseDto> getProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }

        return productResponseDtos;
    }

    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(()->new NullPointerException("해당 상품은 존재하지 않습니다."));
        product.updateByItemDto(itemDto);
    }
}
