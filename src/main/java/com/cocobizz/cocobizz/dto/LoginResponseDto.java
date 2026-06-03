package com.cocobizz.cocobizz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String message;
    private String role;
    private String redirectUrl;
    private String email;
    private Long userId;

}