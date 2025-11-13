package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer id;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Size(max = 255)
    @Column(name = "order_info")
    private String orderInfo;

    @ColumnDefault("sysdatetime()")
    @Column(name = "paymentdate")
    private Instant paymentDate;

    @Size(max = 50)
    @Column(name = "paymentmethod", length = 50)
    private String paymentMethod;

    @Size(max = 20)
    @ColumnDefault("'PENDING'")
    @Column(name = "paymentstatus", length = 20)
    private String paymentStatus;

    @Size(max = 100)
    @Column(name = "transactioncode", length = 100)
    private String transactionCode;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Size(max = 20)
    @Column(name = "vnp_bank_code", length = 20)
    private String vnpBankCode;

    @Size(max = 255)
    @Column(name = "vnp_bank_tran_no")
    private String vnpBankTranNo;

    @Size(max = 20)
    @Column(name = "vnp_card_type", length = 20)
    private String vnpCardType;

    @Size(max = 14)
    @Column(name = "vnp_pay_date", length = 14)
    private String vnpPayDate;

    @Size(max = 2)
    @Column(name = "vnp_response_code", length = 2)
    private String vnpResponseCode;

    @Size(max = 100)
    @Column(name = "vnp_transaction_no", length = 100)
    private String vnpTransactionNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionid")
    private Transaction transaction;

}