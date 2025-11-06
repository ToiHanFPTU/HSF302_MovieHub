package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.TransactionHistory;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.repository.TransactionHistoryRepository;
import com.g_wuy.hsf302.moviehub.repository.UserRepository;
import com.g_wuy.hsf302.moviehub.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionHistoryRepository transactionHistoryRepository,
                                  UserRepository userRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<TransactionHistory> findAllByTransactionID_UserIDAndStatus(int userId, String status) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if(status != null && !status.isEmpty()) {
                // Filter by status if provided
                return transactionHistoryRepository.findAllByTransactionID_UserID(user.getId()).stream()
                        .filter(th -> th.getStatus().equalsIgnoreCase(status))
                        .toList();
            }
            return transactionHistoryRepository.findAllByTransactionID_UserID(user.getId());
        }
        return null;
    }
}
