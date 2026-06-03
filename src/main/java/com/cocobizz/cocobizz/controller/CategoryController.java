package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.CategoryDTO;
import com.cocobizz.cocobizz.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    // CREATE
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestParam String categoryName,
            @RequestParam String description,
            @RequestParam MultipartFile image
    ){

        CategoryDTO created =
                categoryService.createCategory(categoryName,description,image);

        return ResponseEntity.status(201).body(created);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){

        return ResponseEntity.ok(categoryService.getAllCategories());

    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id){

        return ResponseEntity.ok(categoryService.getCategoryById(id));

    }

    // UPDATE
    @PutMapping(value="/{id}", consumes="multipart/form-data")
    public ResponseEntity<CategoryDTO> updateCategory(

            @PathVariable Long id,
            @RequestParam String categoryName,
            @RequestParam String description,
            @RequestParam(required=false) MultipartFile image

    ){

        return ResponseEntity.ok(
                categoryService.updateCategory(id,categoryName,description,image)
        );
    }

    // DELETE (SOFT DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){

        categoryService.deleteCategory(id);

        return ResponseEntity.ok("Category deleted successfully");
    }

}