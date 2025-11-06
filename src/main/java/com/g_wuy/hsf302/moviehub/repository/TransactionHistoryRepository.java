package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Transaction;
import com.g_wuy.hsf302.moviehub.entity.TransactionHistory;
import com.g_wuy.hsf302.moviehub.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,Integer> {
    @Query("SELECT th FROM TransactionHistory th WHERE th.transactionID.userID = :userId" +
            " ORDER BY th.updatedAt DESC")
    List<TransactionHistory> findAllByTransactionID_UserID(int userId);
}
