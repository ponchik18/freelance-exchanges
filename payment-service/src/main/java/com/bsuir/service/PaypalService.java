package com.bsuir.service;

import com.bsuir.dto.payment.PaymentRequest;
import com.bsuir.dto.payment.PayoutRequest;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutItem;
import com.paypal.api.payments.PayoutSenderBatchHeader;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.APIContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;

    public Payment createPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(paymentRequest.getCurrency());
        amount.setTotal(String.format(Locale.forLanguageTag(paymentRequest.getCurrency()), "%.2f", paymentRequest.getTotal())); // 9.99$

        Transaction transaction = new Transaction();
        transaction.setDescription(paymentRequest.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payment payment = getPayment(paymentRequest, transactionList);

        return payment.create(apiContext);
    }

    private Payment getPayment(PaymentRequest paymentRequest, List<Transaction> transactionList) {
        Payer payer = new Payer();
        payer.setPaymentMethod(paymentRequest.getMethod());

        Payment payment = new Payment();
        payment.setIntent(paymentRequest.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactionList);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(paymentRequest.getCancelUrl());
        redirectUrls.setReturnUrl(paymentRequest.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

    public void executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        payment.execute(apiContext, paymentExecution);
    }

    public PayoutBatch getPayout(PayoutRequest payoutRequest) throws PayPalRESTException {
        Payout payout = new Payout();
        PayoutSenderBatchHeader payoutSenderBatchHeader = new PayoutSenderBatchHeader();
        payoutSenderBatchHeader.setRecipientType("EMAIL");
        payoutSenderBatchHeader.setEmailSubject("Test payout");
        payoutSenderBatchHeader.setSenderBatchId("Test_sdk_" + RandomStringUtils.randomAlphanumeric(7));
        payout.setSenderBatchHeader(payoutSenderBatchHeader);
        payout.setItems(buildPayoutItems(payoutRequest));
        return payout.create(apiContext, null);
    }

    private List<PayoutItem> buildPayoutItems(PayoutRequest payoutRequest) {
        PayoutItem payoutItem = new PayoutItem();
        payoutItem.setRecipientType("EMAIL");
        payoutItem.setReceiver("recipient@example.com");

        Currency currency = new Currency(payoutRequest.getCurrency(), String.format(Locale.forLanguageTag(payoutRequest.getCurrency()), "%.2f", payoutRequest.getTotal()));
        payoutItem.setAmount(currency);
        payoutItem.setNote("Any payout");
        payoutItem.setSenderItemId("item_" + System.currentTimeMillis());
        return List.of(payoutItem);
    }
}