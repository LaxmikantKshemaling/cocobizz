package com.cocobizz.cocobizz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String PROFILE_PATH = "file:C:/cocobizz/uploads/profile/";
    private final String CATEGORY_PATH = "file:C:/cocobizz/uploads/category/";
    private final String PRODUCT_PATH = "file:C:/cocobizz/uploads/products/";

    private final String DC_PATH = "file:C:/cocobizz/dc/";

    private final String BARCODE_PATH = "file:C:/cocobizz/barcodes/";

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        // ✅ MUST USE file:/ (IMPORTANT)
        registry.addResourceHandler("/barcodes/**")
                .addResourceLocations("file:/C:/cocobizz/barcodes/");

        registry.addResourceHandler("/dc/**")
                .addResourceLocations("file:/C:/cocobizz/dc/");

        registry.addResourceHandler("/profile-images/**")
                .addResourceLocations(PROFILE_PATH);

        registry.addResourceHandler("/category-images/**")
                .addResourceLocations(CATEGORY_PATH);

        registry.addResourceHandler("/product-images/**")
                .addResourceLocations(PRODUCT_PATH);


    }
}