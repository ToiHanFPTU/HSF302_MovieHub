package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.repository.UserRepository;
import com.g_wuy.hsf302.moviehub.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByPasswordAndEmail(String password, String email) {
        return userRepository.findUserByPasswordHashAndEmail(password, email);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByFullName(username);
    }

    @Override
    public User createUser(String fullName, String email, String password, String phone, String role) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setPhone(phone);
        user.setRole(role);
        return userRepository.save(user);
    }
}
