package com.sungjin.reviewroom.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sungjin.reviewroom.entity.Show;


public interface ShowRepository extends JpaRepository<Show, Integer>  {
    
}
