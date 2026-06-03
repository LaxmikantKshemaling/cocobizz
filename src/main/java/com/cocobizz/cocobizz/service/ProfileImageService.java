package com.cocobizz.cocobizz.service;


import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

    String uploadImage(String email, MultipartFile  file);
    String updateImage(String  email, MultipartFile  file);
    String deleteImage(String email);
}
