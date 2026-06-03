package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.entity.Users;
import com.cocobizz.cocobizz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/profile/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public String uploadImage(String email, MultipartFile file) {

        // 1️⃣ Validate file exists
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("No file selected");
        }

        // 2️⃣ Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds 5MB limit");
        }

        // 3️⃣ Validate file type
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/jpg"))) {
            throw new RuntimeException("Only JPG and PNG images are allowed");
        }

        // 4️⃣ Fetch user
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5️⃣ Create directory if not exists
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 6️⃣ Generate unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            file.transferTo(new File(UPLOAD_DIR + fileName));
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }

        // 7️⃣ Save filename in DB
        user.setProfileImage(fileName);
        userRepository.save(user);

        return "Profile image uploaded successfully";
    }

    @Override
    public String updateImage(String email, MultipartFile file) {

        // Delete old image first (safe delete)
        deleteImage(email);

        // Upload new image
        return uploadImage(email, file);
    }

    @Override
    public String deleteImage(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImage() != null) {

            File file = new File(UPLOAD_DIR + user.getProfileImage());

            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("Failed to delete old image");
                }
            }

            user.setProfileImage(null);
            userRepository.save(user);
        }

        return "Profile image deleted successfully";
    }
}