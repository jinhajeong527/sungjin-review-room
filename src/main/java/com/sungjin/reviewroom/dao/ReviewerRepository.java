package com.sungjin.reviewroom.dao;

import com.sungjin.reviewroom.entity.Reviewer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer, Integer> {
    
}
