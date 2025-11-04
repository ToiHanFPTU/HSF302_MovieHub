package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.Category;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // -------------------- CUSTOMER --------------------
    @GetMapping("/categories")
    public String listCategoriesForCustomer(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home"; // hoặc "category_list_user" nếu bạn tách riêng
    }

    // -------------------- ADMIN --------------------
    @GetMapping("/admin/categories")
    public String listCategories(Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        model.addAttribute("categories", categoryService.getAllCategories());
        return "manager_category/list_category";
    }

    @GetMapping("/admin/categories/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        model.addAttribute("category", new Category());
        return "manager_category/create_category";
    }

    @PostMapping("/admin/categories/add")
    public String addCategory(@ModelAttribute Category category, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        Category cat = categoryService.getCategoryById(id);
        if (cat == null) return "redirect:/admin/categories";
        model.addAttribute("category", cat);
        return "manager_category/update_category";
    }

    @PostMapping("/admin/categories/edit")
    public String editCategory(@ModelAttribute Category category, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    // -------------------- HELPER --------------------
    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        User u = (User) session.getAttribute("user");
        return u != null && "Admin".equalsIgnoreCase(u.getRole());
    }
}
