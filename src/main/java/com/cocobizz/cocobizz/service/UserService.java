package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.UserDto;
import com.cocobizz.cocobizz.entity.Role;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(Long currentUserId);

    UserDto getUserById(Long userId, Long currentUserId);

    UserDto activateUser(Long userId, Long currentUserId);

    UserDto deactivateUser(Long userId, Long currentUserId);

    UserDto updateUser(Long userId, UserDto dto, Long currentUserId);

    void deleteUser(Long userId, Long currentUserId);

}