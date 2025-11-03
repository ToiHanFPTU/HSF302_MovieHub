package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.repository.MovieRepository;
import com.g_wuy.hsf302.moviehub.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Movie movie) {
        Movie existingMovie = movieRepository.findById(movie.getId()).orElse(null);
        if (existingMovie != null)
            movieRepository.delete(existingMovie);
    }

    @Override
    public void updateMovie(Movie movie) {
        Movie existingMovie = movieRepository.findById(movie.getId()).orElse(null);
        if (existingMovie != null) {
            existingMovie.setTitle(movie.getTitle());
            existingMovie.setDescription(movie.getDescription());
            existingMovie.setDuration(movie.getDuration());
            existingMovie.setReleaseDate(movie.getReleaseDate());
            existingMovie.setLanguage(movie.getLanguage());
            movieRepository.save(movie);
        }
    }
}
