package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.ProductDto;
import com.cocobizz.cocobizz.security.SecurityUtil;
import com.cocobizz.cocobizz.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService service;
    private final SecurityUtil securityUtil;

    @PostMapping(consumes = "multipart/form-data")
    public ProductDto create(
            @RequestParam Long stockInflowId,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam Double unitPrice,
            @RequestParam String unitType,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image,
            HttpServletRequest request
    ){
        Long userId = securityUtil.getCurrentUserId(request);

        return service.createProduct(
                stockInflowId,
                name,
                categoryId,
                unitPrice,
                unitType,
                description,
                image,
                userId
        );
    }

    @GetMapping
    public List<ProductDto> getAll(){
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable Long id){
        return service.getProductById(id);
    }

    @PutMapping(value="/{id}", consumes="multipart/form-data")
    public ProductDto update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam Double unitPrice,
            @RequestParam String unitType,
            @RequestParam String description,
            @RequestParam(required=false) MultipartFile image
    ){
        return service.updateProduct(id,name,categoryId,unitPrice,unitType,description,image);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        service.deleteProduct(id);
        return "Deleted";
    }

    @GetMapping("/category/{id}")
    public List<ProductDto> byCategory(@PathVariable Long id){
        return service.getProductsByCategory(id);
    }

    @GetMapping("/search")
    public List<ProductDto> search(@RequestParam String name){
        return service.searchProducts(name);
    }
}