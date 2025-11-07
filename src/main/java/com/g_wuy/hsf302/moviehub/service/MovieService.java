package com.g_wuy.hsf302.moviehub.service;


import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.model.response.MovieResponse;

import java.util.List;


public interface MovieService {
    List<MovieResponse> getAllMovies();
    Movie getMovieById(Integer id);
    Movie saveMovieWithCategories(Movie movie, List<Integer> categoryIds);
    Movie updateMovieWithCategories(Movie movie, List<Integer> categoryIds);
    void deleteMovie(Integer id);
}
