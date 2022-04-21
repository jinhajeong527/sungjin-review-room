package com.sungjin.reviewroom.dao;

import java.util.Optional;

import com.sungjin.reviewroom.entity.Role;
import com.sungjin.reviewroom.model.EnumRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(EnumRole name);
}
