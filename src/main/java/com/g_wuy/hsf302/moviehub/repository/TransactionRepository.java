package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByStatusAndPaymentMethod(String status, String method);

    List<Transaction> findAllBy();

    // Find by user ID
    List<Transaction> findByUserId(Integer userId);

    // Find by user ID and status
    List<Transaction> findByUserIdAndStatus(Integer userId, String status);

    // Find by user ID and payment method
    List<Transaction> findByUserIdAndPaymentMethod(Integer userId, String method);

    // Find by user ID, status and payment method
    List<Transaction> findByUserIdAndStatusAndPaymentMethod(Integer userId, String status, String method);
}
