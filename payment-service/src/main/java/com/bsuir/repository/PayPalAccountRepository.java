package com.bsuir.repository;

import com.bsuir.entity.PayPalAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayPalAccountRepository extends JpaRepository<PayPalAccount, Long> {
    Optional<PayPalAccount> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByPaypalEmail(String paypalEmail);
}