package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
