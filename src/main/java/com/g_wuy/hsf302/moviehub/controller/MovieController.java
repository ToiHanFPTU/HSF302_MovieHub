package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.service.CategoryService;
import com.g_wuy.hsf302.moviehub.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private CategoryService categoryService;

    /* ---------- CUSTOMER: xem danh sách phim ---------- */
    @GetMapping("/movies")
    public String listForCustomer(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "home"; // Trang người dùng xem phim
    }

    /* ---------- ADMIN: danh sách phim ---------- */
    @GetMapping("/admin/movies")
    public String listMovies(Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        model.addAttribute("movies", movieService.getAllMovies());
        return "manager_movie/list_movie";
    }

    /* ---------- ADMIN: thêm phim ---------- */
    @GetMapping("/admin/movies/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        model.addAttribute("movie", new Movie());
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "manager_movie/create_movie";
    }

    @PostMapping("/admin/movies/add")
    public String addMovie(@ModelAttribute Movie movie,
                           @RequestParam(required = false, name = "categoryIds") List<Integer> categoryIds,
                           HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        movieService.saveMovieWithCategories(movie, categoryIds);
        return "redirect:/admin/movies"; // Chuyển hướng về danh sách phim
    }

    /* ---------- ADMIN: chỉnh sửa phim ---------- */
    @GetMapping("/admin/movies/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        Movie movie = movieService.getMovieById(id);
        if (movie == null) return "redirect:/admin/movies";
        model.addAttribute("movie", movie);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "manager_movie/update_movie";
    }

    @PostMapping("/admin/movies/edit")
    public String editMovie(@ModelAttribute Movie movie,
                            @RequestParam(required = false, name = "categoryIds") List<Integer> categoryIds,
                            HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        movieService.updateMovieWithCategories(movie, categoryIds);
        return "redirect:/admin/movies"; // Trở lại danh sách
    }

    /* ---------- ADMIN: xóa phim ---------- */
    @GetMapping("/admin/movies/delete/{id}")
    public String deleteMovie(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        movieService.deleteMovie(id);
        return "redirect:/admin/movies"; // Trở lại danh sách
    }

    /* ---------- Helper: kiểm tra admin ---------- */
    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        User u = (User) session.getAttribute("user");
        return u != null && "Admin".equalsIgnoreCase(u.getRole());
    }
}
