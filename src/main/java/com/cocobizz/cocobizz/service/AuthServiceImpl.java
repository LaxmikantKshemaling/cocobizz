package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.*;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    // =========================================
    // SIGNUP WITH ROLE
    // =========================================
    @Override
    public SignupResponseDto signup(SignupRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User already exists with this email");
        }

        Users user = new Users();
        user.setUserName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 🔥 ROLE HANDLING
        if(dto.getRole() == Role.ADMIN){
            if(!userRepository.findByRole(Role.ADMIN).isEmpty()){
                throw new RuntimeException("Admin already exists");
            }
        }

        user.setRole(dto.getRole());
        user.setAddress(dto.getAddress()); // ✅ NEW
        user.setVerified(true);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Welcome to Cocobizz",
                "Hi " + user.getUserName() +
                        ", your account has been created as " +
                        user.getRole().name()
        );

        return SignupResponseDto.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        Users user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        if (!Boolean.TRUE.equals(user.getActive())) {

            throw new RuntimeException("Account is inactive");
        }

        if (!Boolean.TRUE.equals(user.getVerified())) {

            throw new RuntimeException("Account not verified");
        }

        String redirectUrl = switch (user.getRole()) {

            case ADMIN -> "/admin/dashboard";

            case FARMER -> "/farmer/dashboard";

            case DISTRIBUTOR -> "/distributor/dashboard";

            case BUYER -> "/buyer/dashboard";

            case SELLER -> "/seller/dashboard";
        };

        return new LoginResponseDto(
                "Login successful",
                user.getRole().name(),
                redirectUrl,
                user.getEmail(),
                user.getUserId()
        );
    }


    // =========================================
    // LOGOUT
    // =========================================
    @Override
    public String logout(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return "Logout successful";
    }

    // =========================================
    // FORGOT PASSWORD
    // =========================================
    @Override
    public void forgotPassword(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        otpTokenRepository.deleteByEmail(email);

        String otp =
                String.valueOf(
                        100000 + new Random().nextInt(900000)
                );

        OtpToken token = OtpToken.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(2))
                .build();

        otpTokenRepository.save(token);

        emailService.sendEmail(
                email,
                "Cocobizz Password Reset OTP",
                "Your OTP is: " + otp
        );
    }

    // =========================================
    // VERIFY OTP
    // =========================================
    @Override
    public void verifyOtp(String email, String otp) {

        OtpToken token = otpTokenRepository
                .findTopByEmailOrderByExpiryTimeDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!token.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
    }

    // =========================================
    // RESET PASSWORD
    // =========================================
    @Override
    public void resetPassword(String email, String newPassword) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpTokenRepository.deleteByEmail(email);
    }





    /* ABSOLUTE PATH */
    private final String uploadDir = "C:/cocobizz/uploads/profile/";

    @Override
    public String updateProfileImage(Long userId, MultipartFile file){

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(file == null || file.isEmpty()){
            throw new RuntimeException("File cannot be empty");
        }

        try{

            /* CREATE DIRECTORY */

            File directory = new File(uploadDir);

            if(!directory.exists()){
                directory.mkdirs();
            }

            /* DELETE OLD IMAGE */

            if(user.getProfileImage()!=null){

                File oldFile = new File(uploadDir + user.getProfileImage());

                if(oldFile.exists()){
                    oldFile.delete();
                }
            }

            /* NEW FILE NAME */

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            File dest = new File(uploadDir + fileName);

            /* SAVE FILE */

            file.transferTo(dest);

            user.setProfileImage(fileName);

            userRepository.save(user);

            return "Profile image updated successfully";

        }catch(Exception e){

            e.printStackTrace();

            throw new RuntimeException("Image update failed");
        }
    }



    @Override
    public String uploadProfileImage(Long userId, MultipartFile file){

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(file == null || file.isEmpty()){
            throw new RuntimeException("File cannot be empty");
        }

        try{

            File directory = new File(uploadDir);

            if(!directory.exists()){
                directory.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            File dest = new File(uploadDir + fileName);

            file.transferTo(dest);

            user.setProfileImage(fileName);

            userRepository.save(user);

            return "Profile image uploaded successfully";

        }catch(Exception e){

            e.printStackTrace();

            throw new RuntimeException("Image upload failed");
        }
    }


    @Override
    public UserDto getUserProfile(Long userId, Long currentUserId){

        if(!userId.equals(currentUserId)){
            throw new RuntimeException("Unauthorized Access");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToDto(user);
    }

    @Override
    public String deleteProfileImage(Long userId){

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getProfileImage()!=null){

            File file = new File(uploadDir + user.getProfileImage());

            if(file.exists()){
                file.delete();
            }

            user.setProfileImage(null);

            userRepository.save(user);

        }

        return "Profile image deleted";

    }


    private UserDto convertToDto(Users user){

        UserDto dto = new UserDto();

        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setActive(user.getActive());
        dto.setProfileImage(user.getProfileImage());

        return dto;
    }

}
