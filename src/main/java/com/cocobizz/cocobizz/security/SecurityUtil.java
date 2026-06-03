package com.cocobizz.cocobizz.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentUserId(HttpServletRequest request) {

        String userIdHeader = request.getHeader("X-USER-ID");

        // ✅ CHECK NULL + EMPTY
        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            throw new RuntimeException("Unauthorized: Missing User ID");
        }

        try {
            Long userId = Long.parseLong(userIdHeader.trim());

            // ✅ DEBUG LOG (VERY IMPORTANT)
            System.out.println("Logged-in User ID: " + userId);

            return userId;

        } catch (NumberFormatException e) {
            throw new RuntimeException("Unauthorized: Invalid User ID");
        }
    }
}