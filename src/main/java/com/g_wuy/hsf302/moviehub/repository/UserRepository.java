package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    User findUserByPasswordHashAndEmail(String passwordHash, String email);

    User findUserByFullName(String username);
}
