package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Product;
import com.cocobizz.cocobizz.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(Category category);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}