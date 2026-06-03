package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.CategoryDTO;
import com.cocobizz.cocobizz.entity.Category;
import com.cocobizz.cocobizz.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    private final String DIR = "C:/cocobizz/uploads/category/";

    @Override
    public CategoryDTO createCategory(String name, String desc, MultipartFile image) {

        if(repo.findByCategoryName(name).isPresent()){
            throw new RuntimeException("Category already exists");
        }

        String file = save(image);

        Category c = Category.builder()
                .categoryName(name)
                .description(desc)
                .categoryImage(file)
                .active(true)
                .build();

        return map(repo.save(c));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return repo.findByActiveTrue().stream().map(this::map).toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return map(repo.findById(id).orElseThrow());
    }

    @Override
    public CategoryDTO updateCategory(Long id, String name, String desc, MultipartFile image) {

        Category c = repo.findById(id).orElseThrow();

        c.setCategoryName(name);
        c.setDescription(desc);

        if(image!=null && !image.isEmpty()){
            delete(c.getCategoryImage());
            c.setCategoryImage(save(image));
        }

        return map(repo.save(c));
    }

    @Override
    public void deleteCategory(Long id) {
        Category c = repo.findById(id).orElseThrow();
        c.setActive(false);
        repo.save(c);
    }


    private String save(MultipartFile file){

        try{

            if(file == null || file.isEmpty()){
                return null; // ✅ IMPORTANT FIX
            }

            File dir = new File(DIR);
            if(!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(DIR + fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        }catch(Exception e){
            e.printStackTrace(); // ✅ DEBUG
            throw new RuntimeException("Image upload failed");
        }
    }

    private void delete(String name){
        if(name==null) return;
        new File(DIR+name).delete();
    }

    private CategoryDTO map(Category c){

        CategoryDTO d = new CategoryDTO();

        d.setId(c.getId());
        d.setCategoryName(c.getCategoryName());
        d.setDescription(c.getDescription());

        if(c.getCategoryImage()!=null){
            d.setCategoryImage("/category-images/"+c.getCategoryImage());
        }

        return d;
    }
}