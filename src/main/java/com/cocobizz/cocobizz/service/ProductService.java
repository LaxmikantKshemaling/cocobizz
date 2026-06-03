package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(
            Long stockInflowId,
            String name,
            Long categoryId,
            Double price,
            String unitType,
            String desc,
            MultipartFile image,
            Long currentUserId
    );

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, String name, Long categoryId, Double price,
                             String unitType, String desc, MultipartFile image);

    void deleteProduct(Long id);

    List<ProductDto> getProductsByCategory(Long categoryId);

    List<ProductDto> searchProducts(String name);
}