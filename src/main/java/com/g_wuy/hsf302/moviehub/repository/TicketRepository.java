package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Seat;
import com.g_wuy.hsf302.moviehub.entity.Showtime;
import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    boolean existsByShowtimeIDAndSeatID(Showtime showtime, Seat seat);

    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.showtimeID s " +
            "JOIN FETCH s.movieID " +
            "JOIN FETCH s.roomID " +
            "JOIN FETCH t.seatID " +
            "WHERE t.transactionID = :transaction")
    List<Ticket> findByTransactionIDWithShowtimeAndMovie(@Param("transaction") Transaction transaction);
    @Query("SELECT t FROM Ticket t WHERE t.showtimeID = :showtime")
    List<Ticket> findByShowtimeID(@Param("showtime") Showtime showtime);
    @Query("SELECT t FROM Ticket t " +
            "JOIN FETCH t.showtimeID s " +
            "JOIN FETCH s.movieID " +
            "JOIN FETCH s.roomID " +
            "JOIN FETCH t.seatID " +
            "WHERE t.id = :ticketId")
    Ticket findByIdWithShowtimeAndMovie(@Param("ticketId") Integer ticketId);
}
