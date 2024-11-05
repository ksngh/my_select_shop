package kr.myselectshop.controller;


import kr.myselectshop.dto.ProductMypriceRequestDto;
import kr.myselectshop.dto.ProductRequestDto;
import kr.myselectshop.dto.ProductResponseDto;
import kr.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto productMypriceRequestDto) {
        return productService.updateProduct(id,productMypriceRequestDto);
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getProducts() {
        return productService.getProducts();
    }
}
