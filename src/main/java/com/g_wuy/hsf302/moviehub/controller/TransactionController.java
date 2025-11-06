package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transaction-histories")
    public String showTransactionHistories(@RequestParam String status, HttpServletRequest req, Model model) {
        HttpSession session = req.getSession(false);
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("transactions", transactionService.findAllByTransactionID_UserIDAndStatus(user.getId(), status));
        return "/transaction_history";
    }

}
