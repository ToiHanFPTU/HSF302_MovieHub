package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.model.dto.request.VNPayRequest;
import com.g_wuy.hsf302.moviehub.model.dto.response.VNPayResponse;
import com.g_wuy.hsf302.moviehub.service.impl.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment/form/{transactionId}")
    public String showPaymentForm(@PathVariable Integer transactionId, Model model) {
        model.addAttribute("transactionId", transactionId);
        model.addAttribute("vnpRequest", new VNPayRequest());
        return "payment-form"; // file payment-form.html
    }

    @PostMapping("/payment/create/{transactionId}")
    public String createPayment(@PathVariable Integer transactionId,
                                @ModelAttribute VNPayRequest vnpRequest,
                                HttpServletRequest servletRequest,
                                Model model) {
        try {
            VNPayResponse res = paymentService.createPayment(vnpRequest, servletRequest, transactionId);
            if ("00".equals(res.getCode())) {
                return "redirect:" + res.getPaymentUrl();
            } else {
                model.addAttribute("error", res.getMessage());
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/payment/return/{transactionId}")
    public String handleVNPayReturn(@PathVariable Integer transactionId,
                                    @RequestParam Map<String, String> params,
                                    Model model) {
        String message = paymentService.handleReturn(params, transactionId);
        model.addAttribute("message", message);
        return "payment-result"; // file payment-result.html
    }
}
