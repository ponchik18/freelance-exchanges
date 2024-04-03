package com.bsuir.service;

import com.bsuir.dto.paypal.PayPalAccountRequest;
import com.bsuir.dto.paypal.PayPalAccountUpdateRequest;
import com.bsuir.entity.PayPalAccount;
import com.bsuir.exception.DuplicateValueException;
import com.bsuir.exception.PayPalAccountNotFoundException;
import com.bsuir.repository.PayPalAccountRepository;
import com.paypal.base.rest.APIContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayPalAccountService {
    private final PayPalAccountRepository payPalAccountRepository;
    private final APIContext apiContext;

    public PayPalAccount getPayPalAccountById(String userId) {
        return payPalAccountRepository.findByUserId(userId).orElseThrow(() -> new PayPalAccountNotFoundException(userId));
    }

    public PayPalAccount createPayPalAccount(PayPalAccountRequest payPalAccountRequest) {
        if (payPalAccountRepository.existsByUserId(payPalAccountRequest.getUserId())) {
            throw new DuplicateValueException("userId", payPalAccountRequest.getUserId());
        }

        if (payPalAccountRepository.existsByPaypalEmail(payPalAccountRequest.getPaypalEmail())) {
            throw new DuplicateValueException("paypalEmail", payPalAccountRequest.getPaypalEmail());
        }

        PayPalAccount payPalAccount = PayPalAccount.builder()
                .paypalEmail(payPalAccountRequest.getPaypalEmail())
                .userId(payPalAccountRequest.getUserId())
                .build();

        return payPalAccountRepository.save(payPalAccount);
    }

    public PayPalAccount updatePayPalAccount(PayPalAccountUpdateRequest payPalAccountUpdateRequest, String userId) {
        PayPalAccount payPalAccount = getPayPalAccountById(userId);

        if (payPalAccount.getPaypalEmail().equals(payPalAccountUpdateRequest.getPaypalEmail())) {
            throw new DuplicateValueException("paypalEmail", payPalAccountUpdateRequest.getPaypalEmail());
        }

        payPalAccount.setPaypalEmail(payPalAccountUpdateRequest.getPaypalEmail());
        return payPalAccountRepository.save(payPalAccount);
    }
}