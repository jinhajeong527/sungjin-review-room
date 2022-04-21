package com.sungjin.reviewroom.dao;

import java.util.Optional;

import com.sungjin.reviewroom.entity.Reviewer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer, Integer> {

    Optional<Reviewer> findByEmail(String email);
    //Boolean existByEmail(String email);

}
