package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.*;
import com.cocobizz.cocobizz.security.SecurityUtil;
import com.cocobizz.cocobizz.service.AuthService;
import com.cocobizz.cocobizz.service.ProfileImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {



    private final AuthService authService;
    private final ProfileImageService profileImageService;
    private  final SecurityUtil securityUtil;





    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto dto) {
        return ResponseEntity.ok(authService.signup(dto));
    }




    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto dto
    ){

        try{

            return ResponseEntity.ok(
                    authService.login(dto)
            );

        }catch(RuntimeException e){

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        }
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String email) {
        return ResponseEntity.ok(authService.logout(email));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("OTP sent successfully");
    }




    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {
        authService.verifyOtp(email, otp);
        return ResponseEntity.ok("OTP verified successfully");
    }





    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String newPassword) {
        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }



    // ================================
    // GET PROFILE
    // ================================
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Long userId,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return ResponseEntity.ok(
                authService.getUserProfile(userId, currentUserId)
        );
    }


    // ================================
// UPLOAD IMAGE (SECURE)
// ================================
    @PostMapping("/upload-profile-image/{userId}")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long userId,
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        if(!userId.equals(currentUserId)){
            throw new RuntimeException("Unauthorized Access");
        }

        return ResponseEntity.ok(
                authService.uploadProfileImage(userId,file)
        );
    }



    // ================================
// UPDATE IMAGE (SECURE)
// ================================
    @PutMapping("/update-profile-image/{userId}")
    public ResponseEntity<?> updateProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        if(!userId.equals(currentUserId)){
            throw new RuntimeException("Unauthorized Access");
        }

        return ResponseEntity.ok(
                authService.updateProfileImage(userId,file)
        );
    }

    // ================================
// DELETE IMAGE (SECURE)
// ================================
    @DeleteMapping("/delete-profile-image/{userId}")
    public ResponseEntity<?> deleteProfileImage(
            @PathVariable Long userId,
            HttpServletRequest request
    ){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        if(!userId.equals(currentUserId)){
            throw new RuntimeException("Unauthorized Access");
        }

        return ResponseEntity.ok(
                authService.deleteProfileImage(userId)
        );
    }

}