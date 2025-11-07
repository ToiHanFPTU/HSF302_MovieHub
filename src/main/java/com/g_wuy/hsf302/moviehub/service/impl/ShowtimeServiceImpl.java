package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Showtime;
import com.g_wuy.hsf302.moviehub.repository.ShowtimeRepository;
import com.g_wuy.hsf302.moviehub.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Override
    public List<Showtime> getShowtimesByMovieId(Integer movieId) {
        return showtimeRepository.findByMovie_Id(movieId);
    }
}
