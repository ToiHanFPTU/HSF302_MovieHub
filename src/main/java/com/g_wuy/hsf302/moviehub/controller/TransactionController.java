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

    @GetMapping("/transactions")
    public String getTransactions(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Transaction> transactions;

        // ADMIN sees all transactions, USER sees only their own
        if ("ADMIN".equals(user.getRole())) {
            transactions = transactionService.getAllTransactions();
            model.addAttribute("isAdmin", true);
        } else {
            transactions = transactionService.getTransactionsByUserId(user.getId());
            model.addAttribute("isAdmin", false);
        }

        model.addAttribute("transactions", transactions);
        return "/payment/transactions";
    }

    @GetMapping("/transactions/filter")
    public String getTransactionsByFilter(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String method,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Transaction> transactions;

        // ADMIN can filter all transactions, USER can only filter their own
        if ("ADMIN".equals(user.getRole())) {
            // Admin filtering
            if (status != null && !status.isEmpty() && method != null && !method.isEmpty()) {
                transactions = transactionService.getTransactionByStatusAndMethod(status, method);
            } else if (status != null && !status.isEmpty()) {
                transactions = transactionService.getAllTransactions().stream()
                        .filter(t -> status.equals(t.getStatus()))
                        .toList();
            } else if (method != null && !method.isEmpty()) {
                transactions = transactionService.getAllTransactions().stream()
                        .filter(t -> method.equals(t.getPaymentMethod()))
                        .toList();
            } else {
                transactions = transactionService.getAllTransactions();
            }
            model.addAttribute("isAdmin", true);
        } else {
            // User filtering (only their own transactions)
            Integer userId = user.getId();
            if (status != null && !status.isEmpty() && method != null && !method.isEmpty()) {
                transactions = transactionService.getTransactionsByUserIdAndStatusAndMethod(userId, status, method);
            } else if (status != null && !status.isEmpty()) {
                transactions = transactionService.getTransactionsByUserIdAndStatus(userId, status);
            } else if (method != null && !method.isEmpty()) {
                transactions = transactionService.getTransactionsByUserIdAndMethod(userId, method);
            } else {
                transactions = transactionService.getTransactionsByUserId(userId);
            }
            model.addAttribute("isAdmin", false);
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedMethod", method);

        return "/payment/transactions";
    }
}
