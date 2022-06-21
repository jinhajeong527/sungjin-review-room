package com.sungjin.reviewroom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Reviewer;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByReviewer(Reviewer reviewer);
}
