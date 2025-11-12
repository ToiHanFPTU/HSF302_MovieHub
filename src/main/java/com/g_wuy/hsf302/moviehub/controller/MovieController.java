package com.g_wuy.hsf302.moviehub.controller;


import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.model.dto.MovieDTO;
import com.g_wuy.hsf302.moviehub.model.request.BookTicketsRequest;
import com.g_wuy.hsf302.moviehub.model.response.BookTicketsResponse;
import com.g_wuy.hsf302.moviehub.model.response.MovieDetailResponse;
import com.g_wuy.hsf302.moviehub.service.CategoryService;
import com.g_wuy.hsf302.moviehub.service.MovieService;
import com.g_wuy.hsf302.moviehub.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TicketService ticketService;

    @GetMapping("/movies")
    public String listForCustomer(Model model) {
        List<MovieDTO> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "home";
    }

    @GetMapping("/admin/movies")
    public String listMovies(Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        List<MovieDTO> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "manager/list_movie";
    }

    @GetMapping("/admin/movies/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        model.addAttribute("movie", new Movie());
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "manager/create_movie";
    }

    @PostMapping("/admin/movies/add")
    public String addMovie(@ModelAttribute Movie movie,
                           @RequestParam(required = false, name = "categoryIds") List<Integer> categoryIds,
                           HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        movieService.saveMovieWithCategories(movie, categoryIds);
        return "redirect:/admin/movies";
    }

    @GetMapping("/admin/movies/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        Movie movie = movieService.getMovieById(id);
        if (movie == null) return "redirect:/admin/movies";
        model.addAttribute("movie", movie);
        model.addAttribute("allCategories", categoryService.getAllCategories());
        return "manager/update_movie";
    }

    @PostMapping("/admin/movies/edit")
    public String editMovie(@ModelAttribute Movie movie,
                            @RequestParam(required = false, name = "categoryIds") List<Integer> categoryIds,
                            HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        movieService.updateMovieWithCategories(movie, categoryIds);
        return "redirect:/admin/movies";
    }

    @GetMapping("/admin/movies/delete/{id}")
    public String deleteMovie(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) return "error/access_denied";
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }

    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        User u = (User) session.getAttribute("user");
        return u != null && "Admin".equalsIgnoreCase(u.getRole());
    }
    @GetMapping("/movies/{id}")
    public String movieDetail(@PathVariable Integer id, Model model) {
        Movie movie = movieService.getMovieById(id);

        MovieDetailResponse response = ticketService.getMovieDetail(id, movie);
        model.addAttribute("movieDetail", response);
        return "booking/movie_detail";
    }
    @GetMapping("/showtime/choose")
    public String chooseSeatsBySelected(@RequestParam Integer showtimeId, Model model) {
        model.addAttribute("availableSeats", ticketService.getAvailableSeats(showtimeId));
        model.addAttribute("showtimeId", showtimeId);
        return "booking/seat_screen";
    }

    @PostMapping("/book")
    public String bookTickets(@ModelAttribute BookTicketsRequest request,
                              HttpSession session,
                              Model model) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/auth/login";
        }

        // Base price chỉ để tham khảo, không cần thiết nữa
        BookTicketsResponse resp = ticketService.bookTickets(sessionUser, request, BigDecimal.ZERO);
        model.addAttribute("booking", resp);

        return "redirect:/movies";
    }


    @GetMapping("/my-tickets")
    public String getMyTickets(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Ticket> tickets = ticketService.getTicketsByUser(user);
        model.addAttribute("tickets", tickets);
        return "booking/my_tickets";
    }

}
