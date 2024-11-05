package kr.myselectshop.service;

import kr.myselectshop.entity.Product;
import kr.myselectshop.entity.ProductRequestDto;
import kr.myselectshop.entity.ProductResponseDto;
import kr.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productRepository.save(new Product(productRequestDto));
        return new ProductResponseDto(product);
    }
}
