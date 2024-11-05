package kr.myselectshop.controller;


import kr.myselectshop.dto.ProductMypriceRequestDto;
import kr.myselectshop.dto.ProductRequestDto;
import kr.myselectshop.dto.ProductResponseDto;
import kr.myselectshop.security.UserDetailsImpl;
import kr.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.createProduct(productRequestDto, userDetails.getUser());
    }

    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto productMypriceRequestDto) {
        return productService.updateProduct(id, productMypriceRequestDto);
    }

    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.getProducts(userDetails.getUser(),page-1,size,sortBy,isAsc);
    }

//    @GetMapping("/admin/products")
//    public List<ProductResponseDto> getAdminProducts() {
//        return productService.getAllProducts();
//    }
}


