package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Transaction;
import com.g_wuy.hsf302.moviehub.repository.TicketRepository;
import com.g_wuy.hsf302.moviehub.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Transaction> getTransactionsByUser(Integer userId) {
        return ticketRepository.findTransactionsByUserId(userId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return ticketRepository.findAllTransactions();
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String status) {
        return ticketRepository.findTransactionsByStatus(status);
    }

    @Override
    public List<Transaction> getTransactionsByUserAndStatus(Integer userId, String status) {
        return ticketRepository.findTransactionsByUserIdAndStatus(userId, status);
    }
}

