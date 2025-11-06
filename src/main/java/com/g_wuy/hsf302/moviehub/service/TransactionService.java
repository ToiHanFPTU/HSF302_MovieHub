package com.g_wuy.hsf302.moviehub.service;


import com.g_wuy.hsf302.moviehub.entity.TransactionHistory;

import java.util.List;

public interface TransactionService {
    public List<TransactionHistory> findAllByTransactionID_UserIDAndStatus(int userId, String status);
}
