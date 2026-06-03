package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.UserDto;
import com.cocobizz.cocobizz.entity.Role;
import com.cocobizz.cocobizz.entity.Users;
import com.cocobizz.cocobizz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private Users getCurrentUser(Long currentUserId){
        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void checkAdmin(Users user){
        if(user.getRole() != Role.ADMIN){
            throw new RuntimeException("Admin access required");
        }
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

        // ✅ FIX
        dto.setAddress(user.getAddress());

        return dto;
    }

    // 🔐 ADMIN ONLY
    @Override
    public List<UserDto> getAllUsers(Long currentUserId){

        Users currentUser = getCurrentUser(currentUserId);
        checkAdmin(currentUser);

        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // 🔐 SELF OR ADMIN
    @Override
    public UserDto getUserById(Long userId, Long currentUserId){

        Users currentUser = getCurrentUser(currentUserId);

        if(!userId.equals(currentUserId) &&
                currentUser.getRole() != Role.ADMIN){
            throw new RuntimeException("Unauthorized Access");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToDto(user);
    }

    // 🔐 ADMIN ONLY
    @Override
    public UserDto activateUser(Long userId, Long currentUserId){

        Users currentUser = getCurrentUser(currentUserId);
        checkAdmin(currentUser);

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(true);
        userRepository.save(user);

        return convertToDto(user);
    }

    // 🔐 ADMIN ONLY
    @Override
    public UserDto deactivateUser(Long userId, Long currentUserId){

        Users currentUser = getCurrentUser(currentUserId);
        checkAdmin(currentUser);

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        userRepository.save(user);

        return convertToDto(user);
    }

    // 🔐 SELF OR ADMIN
    @Override
    public UserDto updateUser(Long userId, UserDto dto, Long currentUserId){

        Users currentUser = getCurrentUser(currentUserId);

        if(!userId.equals(currentUserId) &&
                currentUser.getRole() != Role.ADMIN){
            throw new RuntimeException("Unauthorized Access");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());

        userRepository.save(user);

        return convertToDto(user);
    }

    // 🔐 ADMIN ONLY
    @Override
    public void deleteUser(Long userId, Long currentUserId){

        Users currentUser = getCurrentUser(currentUserId);
        checkAdmin(currentUser);

        // ❗ prevent admin deleting self
        if(userId.equals(currentUserId)){
            throw new RuntimeException("Admin cannot delete own account");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}