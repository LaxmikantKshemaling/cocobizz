package com.cocobizz.cocobizz.dto;

import com.cocobizz.cocobizz.entity.Role;
import lombok.Data;

@Data
public class SignupRequestDto {

    private String name;
    private String email;
    private String password;
    private String address;
    private Role role;


}