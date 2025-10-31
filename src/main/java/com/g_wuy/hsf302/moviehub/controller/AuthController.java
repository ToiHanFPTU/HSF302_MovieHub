package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String root() {
        return "auth/login";  // trả về file home.html
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";  // trả về file login.html
    }

    @GetMapping("/auth/register")
    public String registerPage() {
        return "auth/register";  // trả về file register.html
    }


    @PostMapping("/auth/login")
    public String doLogin(@RequestParam String email,
                           @RequestParam String password,
                           HttpServletRequest request) {
        User user = userService.getUserByEmail(email);
        boolean match = passwordEncoder.matches(password, user.getPasswordHash());
        if (!match) {
            request.setAttribute("message", "Invalid username or password");
            return "auth/login";
        }

        String url = "";
        if(user.getRole().equals("Admin")){
            url = "admin/dashboard";
        } else {
            url = "home";
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);

        return url;
    }

    @PostMapping("/auth/register")
    public String doRegister(@RequestParam String email,
                          @RequestParam String password,
                          @RequestParam(required = false) String phone,
                          @RequestParam(required = false) String fullName,
                          HttpServletRequest request) {
        String message = "";
        String url = "";
        userService.getUserByEmail(email);
        if (userService.getUserByEmail(email) != null) {
            // User exists, proceed with login
            message = "Your email already exist in";
            url = "auth/register";
        } else {
            // User does not exist, create a new user
            message = "register successfully";
            userService.createUser(fullName, email, password, phone, "Member");
            url = "auth/login";
        }
        request.setAttribute("message", message);
        return url;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "auth/login";
    }

    @PostMapping("/current-user")
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }

}
