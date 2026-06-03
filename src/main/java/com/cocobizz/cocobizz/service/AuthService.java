package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AuthService {

    SignupResponseDto signup(SignupRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    String logout(String email);

    void forgotPassword(String email);

    void verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);



    String uploadProfileImage(Long userId, MultipartFile file);

    String updateProfileImage(Long userId, MultipartFile file);

    String deleteProfileImage(Long userId);

    UserDto getUserProfile(Long userId, Long currentUserId);


}