package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"Transaction\"")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @NotNull
    @Column(name = "TotalAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 50)
    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Column(name = "TransactionDate")
    private Instant transactionDate;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Status", length = 50)
    private String status;

}