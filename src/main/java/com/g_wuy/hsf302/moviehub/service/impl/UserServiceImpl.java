package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.repository.UserRepository;
import com.g_wuy.hsf302.moviehub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByPasswordAndEmail(String password, String email) {
        return (User) userRepository.findUserByPasswordHashAndEmail(password, email).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return (User) userRepository.findUserByEmail(email).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return (User) userRepository.findUserByFullName(username).orElse(null);
    }

    @Override
    public User createUser(String fullName, String email, String password, String phone, String role) {
        String passwordHash = passwordEncoder.encode(password);
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setPhone(phone);
        user.setRole(role);
        return userRepository.save(user);
    }
}
