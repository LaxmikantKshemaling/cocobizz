package com.cocobizz.cocobizz.dto;

import com.cocobizz.cocobizz.entity.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long userId;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private Role role;
    private Boolean active;
    private String profileImage;

}