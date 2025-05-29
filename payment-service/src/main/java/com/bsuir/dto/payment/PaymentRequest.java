package com.bsuir.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String currency;
    private String customerId;
    private String freelancerId;
    private Long jobId;
    private BigDecimal amount;
    private String paypalOrderId;
}