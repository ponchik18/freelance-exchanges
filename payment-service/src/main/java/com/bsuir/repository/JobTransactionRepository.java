package com.bsuir.repository;

import com.bsuir.entity.JobTransaction;
import com.bsuir.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobTransactionRepository extends JpaRepository<JobTransaction, Long> {
    Optional<JobTransaction> findByPaypalOrderId(String id);
    List<JobTransaction> findAllByFreelancerIdAndTransactionStatus(String freelancerId, TransactionStatus jobTransaction);
    List<JobTransaction> findAllByCustomerId(String customerId);
}