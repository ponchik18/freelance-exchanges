package com.bsuir.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String customerId;
    private String freelancerId;
    private Long jobId;
    private BigDecimal amount;
}