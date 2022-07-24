package com.sungjin.reviewroom.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sungjin.reviewroom.entity.RefreshToken;
import com.sungjin.reviewroom.entity.Reviewer;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);
    int deleteByReviewer(Reviewer reviewer);
}
