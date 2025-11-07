package com.g_wuy.hsf302.moviehub.model.response;

import java.time.LocalDate;

public interface MovieResponse {
    Integer getMovieId();
    String getTitle();
    String getDescription();
    Integer getDuration();
    LocalDate getReleaseDate();
    String getLanguage();
    String getCategories();
}