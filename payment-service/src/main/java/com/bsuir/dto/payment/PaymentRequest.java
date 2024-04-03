package com.bsuir.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private Double total;
    private String Currency;
    private String method;
    private String intent;
    private String description;
    private String cancelUrl;
    private String successUrl;
}