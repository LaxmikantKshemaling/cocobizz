package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Role;
import com.cocobizz.cocobizz.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Users> findByRole(Role role);   // 🔥 ROLE BASED USERS

}