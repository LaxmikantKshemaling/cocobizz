package com.cocobizz.cocobizz.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private Long id;
    private String categoryName;
    private String description;
    private String categoryImage;
}