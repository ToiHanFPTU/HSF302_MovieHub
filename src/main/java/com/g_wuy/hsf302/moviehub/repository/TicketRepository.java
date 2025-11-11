package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUserOrderByBookingTimeDesc(User user);
    List<Ticket> findByShowtimeId(Integer showtimeId);
    boolean existsByShowtimeIdAndSeatId(Integer showtimeId, Integer seatId);
}
