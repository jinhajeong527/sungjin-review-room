package com.sungjin.reviewroom.dao;

import com.sungjin.reviewroom.entity.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    
}