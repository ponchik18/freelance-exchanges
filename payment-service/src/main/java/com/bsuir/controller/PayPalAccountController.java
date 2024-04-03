package com.bsuir.controller;

import com.bsuir.dto.paypal.PayPalAccountRequest;
import com.bsuir.dto.paypal.PayPalAccountUpdateRequest;
import com.bsuir.entity.PayPalAccount;
import com.bsuir.service.PayPalAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("paypal-accounts")
@RequiredArgsConstructor
public class PayPalAccountController {
    private final PayPalAccountService payPalAccountService;

    @GetMapping("/{userId}")
    public PayPalAccount getPayPalAccountForUser(@PathVariable String userId) {
        return payPalAccountService.getPayPalAccountById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PayPalAccount createPayPalAccount(@RequestBody @Valid PayPalAccountRequest payPalAccountRequest) {
        return payPalAccountService.createPayPalAccount(payPalAccountRequest);
    }

    @PutMapping("/{userId}")
    public PayPalAccount updatePayPal(@PathVariable String userId, @RequestBody @Valid PayPalAccountUpdateRequest payPalAccountUpdateRequest) {
        return payPalAccountService.updatePayPalAccount(payPalAccountUpdateRequest, userId);
    }
}