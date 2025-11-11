package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactionsByUser(Integer userId);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByStatus(String status);
    List<Transaction> getTransactionsByUserAndStatus(Integer userId, String status);
}

