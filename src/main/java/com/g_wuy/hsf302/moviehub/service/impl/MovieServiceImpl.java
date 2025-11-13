package com.g_wuy.hsf302.moviehub.service.impl;


import com.g_wuy.hsf302.moviehub.entity.Category;
import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.entity.MovieCategory;
import com.g_wuy.hsf302.moviehub.entity.MovieCategoryId;
import com.g_wuy.hsf302.moviehub.entity.Showtime;
import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.model.dto.MovieDTO;
import com.g_wuy.hsf302.moviehub.repository.CategoryRepository;
import com.g_wuy.hsf302.moviehub.repository.MovieRepository;
import com.g_wuy.hsf302.moviehub.repository.ShowtimeRepository;
import com.g_wuy.hsf302.moviehub.repository.TicketRepository;
import com.g_wuy.hsf302.moviehub.service.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDTO> result = new ArrayList<>();
        for (Movie m : movies) {
            result.add(convertToDTO(m));
        }
        return result;
    }

    @Override
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Movie saveMovieWithCategories(Movie movie, List<Integer> categoryIds) {
        Set<MovieCategory> movieCategories = new HashSet<>();
        if (categoryIds != null) {
            for (Integer catId : categoryIds) {
                categoryRepository.findById(catId).ifPresent(category -> {
                    MovieCategory mc = new MovieCategory();
                    MovieCategoryId mcId = new MovieCategoryId();
                    mcId.setMovieId(null);
                    mcId.setCategoryId(catId);
                    mc.setId(mcId);
                    mc.setMovie(movie);
                    mc.setCategory(category);
                    movieCategories.add(mc);
                });
            }
        }
        movie.setMovieCategories(movieCategories);
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovieWithCategories(Movie movie, List<Integer> categoryIds) {
        Movie existing = movieRepository.findById(movie.getId()).orElse(null);
        if (existing == null) return null;

        existing.setTitle(movie.getTitle());
        existing.setDescription(movie.getDescription());
        existing.setDuration(movie.getDuration());
        existing.setReleaseDate(movie.getReleaseDate());
        existing.setLanguage(movie.getLanguage());
        existing.setImageUrl(movie.getImageUrl());

        existing.getMovieCategories().clear();

        if (categoryIds != null) {
            for (Integer catId : categoryIds) {
                categoryRepository.findById(catId).ifPresent(category -> {
                    MovieCategory mc = new MovieCategory();
                    MovieCategoryId mcId = new MovieCategoryId();
                    mcId.setMovieId(existing.getId());
                    mcId.setCategoryId(catId);
                    mc.setId(mcId);
                    mc.setMovie(existing);
                    mc.setCategory(category);
                    existing.getMovieCategories().add(mc);
                });
            }
        }

        return movieRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            return;
        }

        // Get all showtimes for this movie
        List<Showtime> showtimes = showtimeRepository.findByMovieIdOrderByStartTime(id);

        // For each showtime, delete associated tickets first
        for (Showtime showtime : showtimes) {
            List<Ticket> tickets = ticketRepository.findByShowtimeId(showtime.getId());
            if (!tickets.isEmpty()) {
                ticketRepository.deleteAll(tickets);
            }
        }

        // Now delete the showtimes
        if (!showtimes.isEmpty()) {
            showtimeRepository.deleteAll(showtimes);
        }

        // Finally delete the movie (movieCategories will be cascade deleted)
        movieRepository.deleteById(id);
    }

    private MovieDTO convertToDTO(Movie movie) {
        List<String> categoryNames = new ArrayList<>();
        if (movie.getCategories() != null) {
            for (Category c : movie.getCategories()) {
                categoryNames.add(c.getCategoryName());
            }
        }
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDuration(),
                movie.getReleaseDate(),
                movie.getLanguage(),
                movie.getImageUrl(),
                categoryNames
        );
    }
}
