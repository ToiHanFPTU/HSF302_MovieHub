package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    // Lấy danh sách Movie theo transactionId
    @Query("SELECT m " +
            "FROM Movie m " +
            "JOIN Showtime s ON s.movie.id = m.id " +
            "JOIN Ticket t ON t.showtime.id = s.id " +
            "WHERE t.transaction.id = :transactionId")
    Movie findMoviesByTransactionId(@Param("transactionId") Integer transactionId);
}
