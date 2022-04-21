package com.sungjin.reviewroom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sungjin.reviewroom.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    
}
