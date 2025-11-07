package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.model.dto.SeatDTO;
import com.g_wuy.hsf302.moviehub.model.request.BookTicketsRequest;
import com.g_wuy.hsf302.moviehub.model.response.BookTicketsResponse;
import com.g_wuy.hsf302.moviehub.model.response.MovieDetailResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TicketService {
    MovieDetailResponse getMovieDetail(Integer movieId, Movie movie);
    List<SeatDTO> getAvailableSeats(Integer showtimeId);
    BookTicketsResponse bookTickets(User user, BookTicketsRequest request, BigDecimal pricePerSeat);
}