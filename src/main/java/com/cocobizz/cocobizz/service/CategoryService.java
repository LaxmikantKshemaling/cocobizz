package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.CategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(String name, String description, MultipartFile image);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO updateCategory(Long id, String name, String description, MultipartFile image);

    void deleteCategory(Long id);
}