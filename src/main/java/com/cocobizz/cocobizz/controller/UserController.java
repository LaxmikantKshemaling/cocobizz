package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.UserDto;
import com.cocobizz.cocobizz.entity.Role;
import com.cocobizz.cocobizz.security.SecurityUtil;
import com.cocobizz.cocobizz.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return ResponseEntity.ok(userService.getAllUsers(currentUserId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long userId,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return ResponseEntity.ok(
                userService.getUserById(userId, currentUserId)
        );
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<?> activateUser(
            @PathVariable Long userId,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return ResponseEntity.ok(
                userService.activateUser(userId, currentUserId)
        );
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(
            @PathVariable Long userId,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return ResponseEntity.ok(
                userService.deactivateUser(userId, currentUserId)
        );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto dto,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        return ResponseEntity.ok(
                userService.updateUser(userId, dto, currentUserId)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId,
            HttpServletRequest request){

        Long currentUserId = securityUtil.getCurrentUserId(request);

        userService.deleteUser(userId, currentUserId);

        return ResponseEntity.ok("User deleted");
    }
}