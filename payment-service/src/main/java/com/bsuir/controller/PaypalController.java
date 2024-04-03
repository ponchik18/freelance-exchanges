package com.bsuir.controller;

import com.bsuir.dto.payment.PaymentRequest;
import com.bsuir.dto.payment.PayoutRequest;
import com.bsuir.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;

    @PostMapping("/create-payment")
    public ResponseEntity<String> createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        try {
            Payment payment = paypalService.createPayment(paymentRequest);
            return ResponseEntity.ok(payment.getId());
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/execute-payment")
    public ResponseEntity<String> executePayment(@RequestParam String paymentId, @RequestParam String payerId) {
        try {
            paypalService.executePayment(paymentId, payerId);
            return ResponseEntity.ok("Payment executed successfully");
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to execute payment");
        }
    }

    @PostMapping("/create-payout")
    public ResponseEntity<String> createPayout(@RequestBody @Valid PayoutRequest payoutRequest) {
        try {
            return ResponseEntity.ok(paypalService.getPayout(payoutRequest).getBatchHeader().getPayoutBatchId());
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }
}