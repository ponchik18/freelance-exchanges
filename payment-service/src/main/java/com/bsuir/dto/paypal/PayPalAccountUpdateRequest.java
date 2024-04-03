package com.bsuir.dto.paypal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PayPalAccountUpdateRequest {
    private String paypalEmail;
}