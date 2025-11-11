package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.Transaction;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * USER: View their own transactions
     */
    @GetMapping("/user/transactions")
    public String getUserTransactions(
            @RequestParam(required = false) String status,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Transaction> transactions;
        if (status != null && !status.isEmpty()) {
            transactions = transactionService.getTransactionsByUserAndStatus(user.getId(), status);
        } else {
            transactions = transactionService.getTransactionsByUser(user.getId());
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("isAdmin", false);
        return "payment/transactions";
    }

    /**
     * ADMIN: View all transactions in the system
     */
    @GetMapping("/admin/transactions")
    public String getAllTransactions(
            @RequestParam(required = false) String status,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/movies";
        }

        List<Transaction> transactions;
        if (status != null && !status.isEmpty()) {
            transactions = transactionService.getTransactionsByStatus(status);
        } else {
            transactions = transactionService.getAllTransactions();
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("isAdmin", true);
        return "payment/transactions";
    }
}

