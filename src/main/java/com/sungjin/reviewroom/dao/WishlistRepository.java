package com.sungjin.reviewroom.dao;

import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.Wishlist;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Set<Wishlist> getByReviewer(Reviewer reviewer);
    Page<Wishlist> findAllByReviewerOrderByCreatedDateDesc(Reviewer reviewer, Pageable pageable); 
}