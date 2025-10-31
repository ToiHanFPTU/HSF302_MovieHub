package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.User;

public interface UserService {
    User getUserById(Integer id);
    User getUserByPasswordAndEmail(String password, String email);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    User createUser(String fullName, String email, String password, String phone, String role);
}
