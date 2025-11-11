package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Transaction;

import java.util.List;

public interface TransactionService {
    // Admin methods - get all transactions
    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionByStatusAndMethod(String status, String method);

    // User methods - get transactions by user ID
    List<Transaction> getTransactionsByUserId(Integer userId);

    List<Transaction> getTransactionsByUserIdAndStatus(Integer userId, String status);

    List<Transaction> getTransactionsByUserIdAndMethod(Integer userId, String method);

    List<Transaction> getTransactionsByUserIdAndStatusAndMethod(Integer userId, String status, String method);
}
