package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Transaction;
import com.g_wuy.hsf302.moviehub.repository.TransactionRepository;
import com.g_wuy.hsf302.moviehub.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllBy();
    }

    @Override
    public List<Transaction> getTransactionByStatusAndMethod(String status, String method) {
        return transactionRepository.findByStatusAndPaymentMethod(status, method);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> getTransactionsByUserIdAndStatus(Integer userId, String status) {
        return transactionRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Transaction> getTransactionsByUserIdAndMethod(Integer userId, String method) {
        return transactionRepository.findByUserIdAndPaymentMethod(userId, method);
    }

    @Override
    public List<Transaction> getTransactionsByUserIdAndStatusAndMethod(Integer userId, String status, String method) {
        return transactionRepository.findByUserIdAndStatusAndPaymentMethod(userId, status, method);
    }
}
