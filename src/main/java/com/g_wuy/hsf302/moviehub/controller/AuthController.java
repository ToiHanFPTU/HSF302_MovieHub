package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping
    public String root() {
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String registerPage() {
        return "auth/register";
    }

    /**
     * Handle login - authenticate user and create session
     */
    @PostMapping("/auth/login")
    public String doLogin(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          HttpServletRequest request,
                          Model model) {
        try {
            // Validate input
            if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                model.addAttribute("message", "Email and password are required");
                return "auth/login";
            }

            // Get user by email
            User user = userService.getUserByEmail(email.trim());

            if (user == null) {
                model.addAttribute("message", "Invalid email or password");
                return "auth/login";
            }

            boolean match = password.equals(user.getPasswordHash());

            if (!match) {
                model.addAttribute("message", "Invalid email or password");
                return "auth/login";
            }

            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            // Redirect based on role
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/home";
            } else {
                return "redirect:/home";
            }

        } catch (Exception e) {
            model.addAttribute("message", "An error occurred during login");
            return "auth/login";
        }
    }

    /**
     * Handle registration - create new user account
     */
    @PostMapping("/auth/register")
    public String doRegister(@RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String fullName,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            // Validate input
            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("message", "Email is required");
                return "auth/register";
            }

            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("message", "Password is required");
                return "auth/register";
            }

            if (password.length() < 6) {
                model.addAttribute("message", "Password must be at least 6 characters");
                return "auth/register";
            }

            // Check if user already exists
            User existingUser = userService.getUserByEmail(email.trim());

            if (existingUser != null) {
                model.addAttribute("message", "Email already exists. Please login instead.");
                return "auth/register";
            }

            // Create new user with role "USER" (not "Member")
            userService.createUser(
                fullName != null ? fullName.trim() : email.split("@")[0],
                email.trim(),
                password, // TODO: Should hash password with BCrypt
                phone != null ? phone.trim() : null,
                "USER" // Fixed: Use "USER" instead of "Member"
            );

            // Redirect to login with success message
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/auth/login";

        } catch (Exception e) {
            model.addAttribute("message", "An error occurred during registration");
            return "auth/register";
        }
    }

    /**
     * Handle logout - invalidate session
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully");
        return "redirect:/auth/login";
    }

    /**
     * Get current logged in user
     */
    @PostMapping("/current-user")
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }
}
