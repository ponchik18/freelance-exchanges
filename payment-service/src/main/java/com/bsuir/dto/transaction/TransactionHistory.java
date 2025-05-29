package com.bsuir.dto.transaction;

import com.bsuir.enums.TransactionHistoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistory {
    private String freelancerId;
    private Long jobId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private TransactionHistoryStatus status;
}
