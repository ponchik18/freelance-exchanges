package com.bsuir.repository;

import com.bsuir.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByFreelancerId(String freelancerId);
}