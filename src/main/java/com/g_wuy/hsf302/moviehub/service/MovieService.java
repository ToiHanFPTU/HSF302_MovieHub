package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {
    public Movie addMovie(Movie movie);
    public void deleteMovie(Movie movie);
    public void updateMovie(Movie movie);
}
