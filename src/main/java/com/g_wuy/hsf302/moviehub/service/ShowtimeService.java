package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Showtime;
import java.util.List;

public interface ShowtimeService {
    List<Showtime> getShowtimesByMovieId(Integer movieId);
}
