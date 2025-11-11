package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.Payment;
import com.g_wuy.hsf302.moviehub.entity.Transaction;
import com.g_wuy.hsf302.moviehub.infratructure.VNPayConfiguration;
import com.g_wuy.hsf302.moviehub.model.request.VNPayRequest;
import com.g_wuy.hsf302.moviehub.model.response.VNPayResponse;
import com.g_wuy.hsf302.moviehub.repository.PaymentRepository;
import com.g_wuy.hsf302.moviehub.repository.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PaymentService {

    @Autowired
    private VNPayConfiguration vnPayConfiguration;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Tạo link thanh toán VNPay
     * @param request VNPay request với amount và order info
     * @param httpRequest HTTP request để lấy IP
     * @param transactionId ID của transaction đã tạo sẵn
     * @return VNPayResponse chứa payment URL
     */
    public VNPayResponse createPayment(VNPayRequest request, HttpServletRequest httpRequest, Integer transactionId) {
        try {
            // Validate transaction exists
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

            String vnp_TxnRef = VNPayConfiguration.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfiguration.getIpAddress(httpRequest);

            String returnUrlWithTxn = vnPayConfiguration.getVnpReturnUrl() + "/" + transactionId;

            Map<String, String> vnp_Params = new TreeMap<>();
            vnp_Params.put("vnp_Version", vnPayConfiguration.getVnpVersion());
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnPayConfiguration.getVnpTmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", request.getOrderInfo());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", returnUrlWithTxn);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            vnp_Params.put("vnp_CreateDate", createDate);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                if (hashData.length() > 0) hashData.append('&');
                hashData.append(entry.getKey()).append('=')
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append('&');
            }

            String vnp_SecureHash = vnPayConfiguration.hmacSHA512(vnPayConfiguration.getSecretKey(), hashData.toString());
            String paymentUrl = vnPayConfiguration.getVnpPayUrl() + "?" + query + "vnp_SecureHash=" + vnp_SecureHash;

            transaction.setPaymentMethod("VNPAY");
            transaction.setStatus("PENDING");
            transaction.setTransactionDate(Instant.now());
            transaction.setTotalAmount(BigDecimal.valueOf(request.getAmount()));
            transactionRepository.save(transaction);

            // ✅ Tạo bản ghi Payment PENDING
            Payment payment = new Payment();
            payment.setTransaction(transaction);
            payment.setPaymentMethod("VNPAY");
            payment.setTransactionCode(vnp_TxnRef);
            payment.setPaymentStatus("PENDING");
            payment.setAmount(BigDecimal.valueOf(request.getAmount()));
            payment.setOrderInfo(request.getOrderInfo());
            payment.setPaymentDate(Instant.now());
            payment.setCreatedAt(Instant.now());
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);

            return VNPayResponse.builder()
                    .code("00")
                    .message("success")
                    .paymentUrl(paymentUrl)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return VNPayResponse.builder()
                    .code("99")
                    .message("Error: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Xử lý callback từ VNPay sau khi user thanh toán
     * @param params Parameters từ VNPay return URL
     * @param transactionId ID của transaction
     * @return Message kết quả
     */
    public String handleReturn(Map<String, String> params, Integer transactionId) {
        try {
            // Validate signature
            String vnpSecureHash = params.get("vnp_SecureHash");
            params.remove("vnp_SecureHash");
            params.remove("vnp_SecureHashType");

            String signValue = vnPayConfiguration.hashAllFields(params);
            if (!signValue.equalsIgnoreCase(vnpSecureHash)) {
                return "Lỗi xác minh chữ ký!";
            }

            // Get transaction
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

            BigDecimal amount = new BigDecimal(params.get("vnp_Amount"))
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);

            // Update transaction
            transaction.setTotalAmount(amount);
            transaction.setPaymentMethod("VNPAY");

            // Find payment by transaction (more efficient)
            Payment payment = paymentRepository.findAll().stream()
                    .filter(p -> p.getTransaction() != null && p.getTransaction().getId().equals(transactionId))
                    .filter(p -> "PENDING".equals(p.getPaymentStatus()))
                    .reduce((first, second) -> second) // Get latest pending payment
                    .orElseGet(() -> {
                        // Create new payment if not found
                        Payment newPayment = new Payment();
                        newPayment.setTransaction(transaction);
                        newPayment.setPaymentMethod("VNPAY");
                        newPayment.setCreatedAt(Instant.now());
                        return newPayment;
                    });

            // Update payment details
            payment.setPaymentDate(Instant.now());
            payment.setUpdatedAt(Instant.now());
            payment.setVnpTransactionNo(params.get("vnp_TransactionNo"));
            payment.setVnpBankCode(params.get("vnp_BankCode"));
            payment.setVnpBankTranNo(params.get("vnp_BankTranNo"));
            payment.setVnpCardType(params.get("vnp_CardType"));
            payment.setVnpPayDate(params.get("vnp_PayDate"));
            payment.setVnpResponseCode(params.get("vnp_ResponseCode"));
            payment.setAmount(amount);
            payment.setOrderInfo(params.get("vnp_OrderInfo"));

            // ✅ Update status based on VNPay response
            String vnpResponseCode = params.get("vnp_ResponseCode");
            if ("00".equals(vnpResponseCode)) {
                transaction.setStatus("SUCCESS");  // Changed from "DONE" to "SUCCESS" for consistency
                payment.setPaymentStatus("COMPLETED");
            } else {
                transaction.setStatus("FAILED");
                payment.setPaymentStatus("FAILED");
            }

            // Save to database
            paymentRepository.save(payment);
            transactionRepository.save(transaction);

            return "Giao dịch " + transaction.getStatus().toLowerCase() + "!";

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi xử lý callback: " + e.getMessage();
        }
    }
}
