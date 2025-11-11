package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.entity.Transaction;
import com.g_wuy.hsf302.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUserOrderByBookingTimeDesc(User user);
    List<Ticket> findByShowtimeId(Integer showtimeId);
    boolean existsByShowtimeIdAndSeatId(Integer showtimeId, Integer seatId);

    // Query để lấy transactions của user thông qua tickets
    @Query("SELECT DISTINCT t.transaction FROM Ticket t WHERE t.user.id = :userId AND t.transaction IS NOT NULL ORDER BY t.transaction.transactionDate DESC")
    List<Transaction> findTransactionsByUserId(@Param("userId") Integer userId);

    // Query để lấy tất cả transactions
    @Query("SELECT DISTINCT t.transaction FROM Ticket t WHERE t.transaction IS NOT NULL ORDER BY t.transaction.transactionDate DESC")
    List<Transaction> findAllTransactions();

    // Query để lấy transactions theo status
    @Query("SELECT DISTINCT t.transaction FROM Ticket t WHERE t.transaction IS NOT NULL AND t.transaction.status = :status ORDER BY t.transaction.transactionDate DESC")
    List<Transaction> findTransactionsByStatus(@Param("status") String status);

    // Query để lấy transactions của user theo status
    @Query("SELECT DISTINCT t.transaction FROM Ticket t WHERE t.user.id = :userId AND t.transaction IS NOT NULL AND t.transaction.status = :status ORDER BY t.transaction.transactionDate DESC")
    List<Transaction> findTransactionsByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);
}
