package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    private Boolean verified;

    private Boolean active;

    @Column(name = "profile_image")
    private String profileImage;

    // ✅ NEW FIELD
    private String address;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();

        if (this.verified == null) {
            this.verified = true;
        }

        if (this.active == null) {
            this.active = true;
        }
    }
}