package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<Object> findUserByPasswordHashAndEmail(String passwordHash, String email);

    Optional<Object> findUserByEmail(String email);

    Optional<Object> findUserByFullName(String username);
}
