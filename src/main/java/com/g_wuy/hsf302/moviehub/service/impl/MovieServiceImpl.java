package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.*;
import com.g_wuy.hsf302.moviehub.repository.CategoryRepository;
import com.g_wuy.hsf302.moviehub.repository.MovieRepository;
import com.g_wuy.hsf302.moviehub.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public Movie saveMovieWithCategories(Movie movie, List<Integer> categoryIds) {
        Set<MovieCategory> movieCategories = new HashSet<>();
        for (Integer catId : categoryIds) {
            Category category = categoryRepository.findById(catId).orElse(null);
            if (category != null) {
                MovieCategory mc = new MovieCategory();
                MovieCategoryId mcId = new MovieCategoryId();
                mcId.setMovieId(movie.getId());
                mcId.setCategoryId(catId);
                mc.setId(mcId);
                mc.setMovie(movie);
                mc.setCategory(category);
                movieCategories.add(mc);
            }
        }
        movie.setMovieCategories(movieCategories);
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovieWithCategories(Movie movie, List<Integer> categoryIds) {
        Movie existingMovie = movieRepository.findById(movie.getId()).orElse(null);
        if (existingMovie == null) return null;

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setDuration(movie.getDuration());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        existingMovie.setLanguage(movie.getLanguage());
        existingMovie.setImageUrl(movie.getImageUrl());

        // ✅ Xóa category cũ, set mới
        existingMovie.getMovieCategories().clear();
        Set<MovieCategory> movieCategories = new HashSet<>();
        for (Integer catId : categoryIds) {
            Category category = categoryRepository.findById(catId).orElse(null);
            if (category != null) {
                MovieCategory mc = new MovieCategory();
                MovieCategoryId mcId = new MovieCategoryId();
                mcId.setMovieId(existingMovie.getId());
                mcId.setCategoryId(catId);
                mc.setId(mcId);
                mc.setMovie(existingMovie);
                mc.setCategory(category);
                movieCategories.add(mc);
            }
        }
        existingMovie.getMovieCategories().addAll(movieCategories);

        return movieRepository.save(existingMovie);
    }

    @Override
    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }
}
