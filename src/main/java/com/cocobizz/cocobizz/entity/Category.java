package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id") // ✅ FIX
    private Long id;

    @Column(name = "category_name", unique = true, nullable = false)
    private String categoryName;

    private String description;

    @Column(name = "category_image")
    private String categoryImage;

    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        if(this.active == null) this.active = true;
    }
}