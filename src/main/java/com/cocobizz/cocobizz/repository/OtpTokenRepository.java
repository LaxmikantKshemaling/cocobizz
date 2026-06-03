package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findTopByEmailOrderByExpiryTimeDesc(String email);

    void deleteByEmail(String email);
}