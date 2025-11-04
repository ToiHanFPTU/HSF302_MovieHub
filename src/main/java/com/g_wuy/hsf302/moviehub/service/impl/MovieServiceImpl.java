package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Category;
import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.repository.CategoryRepository;
import com.g_wuy.hsf302.moviehub.repository.MovieRepository;
import com.g_wuy.hsf302.moviehub.service.MovieService;
import jakarta.transaction.Transactional;
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
    private  CategoryRepository categoryRepository;

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Movie saveMovieWithCategories(Movie movie, List<Integer> categoryIds) {
        Set<Category> cats = new HashSet<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            cats.addAll(categoryRepository.findAllById(categoryIds));
        }
        movie.setCategories(cats);
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovieWithCategories(Movie movie, List<Integer> categoryIds) {
        Movie exist = movieRepository.findById(movie.getId()).orElse(null);
        if (exist == null) return null;
        exist.setTitle(movie.getTitle());
        exist.setDescription(movie.getDescription());
        exist.setDuration(movie.getDuration());
        exist.setReleaseDate(movie.getReleaseDate());
        exist.setLanguage(movie.getLanguage());
        Set<Category> cats = new HashSet<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            cats.addAll(categoryRepository.findAllById(categoryIds));
        }
        exist.setCategories(cats);
        return movieRepository.save(exist);
    }

    @Override
    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }
}
