package com.bsuir.service;

import com.bsuir.dto.freelancer.FreelancerBalance;
import com.bsuir.dto.payment.PaymentRequest;
import com.bsuir.dto.payment.PayoutRequest;
import com.bsuir.dto.transaction.TransactionHistory;
import com.bsuir.entity.JobTransaction;
import com.bsuir.enums.TransactionHistoryStatus;
import com.bsuir.enums.TransactionStatus;
import com.bsuir.exception.NotEnoughMoneyException;
import com.bsuir.feign.FreelancerFeignClient;
import com.bsuir.feign.JobFeignClient;
import com.bsuir.repository.JobTransactionRepository;
import com.bsuir.repository.TransactionRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;
    private final JobTransactionRepository jobTransactionRepository;
    private final JobFeignClient jobFeignClient;
    private final TransactionRepository transactionRepository;
    private final FreelancerFeignClient freelancerFeignClient;

    public Payment createPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(paymentRequest.getCurrency());
        amount.setTotal(String.format(Locale.forLanguageTag(paymentRequest.getCurrency()), "%.2f", paymentRequest.getAmount().multiply(new BigDecimal("1.1")))); // 9.99$

        Transaction transaction = new Transaction();
//        transaction.setDescription(paymentRequest.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        Payment payment = getPayment(paymentRequest, transactionList);

        payment = payment.create(apiContext);

        jobTransactionRepository.save(JobTransaction.builder()
                .jobId(paymentRequest.getJobId())
                .freelancerId(paymentRequest.getFreelancerId())
                .customerId(paymentRequest.getCustomerId())
                .paypalOrderId(payment.getId())
                .transactionStatus(TransactionStatus.CREATED)
                .amount(paymentRequest.getAmount())
                .build());
        return payment;
    }

    private Payment getPayment(PaymentRequest paymentRequest, List<Transaction> transactionList) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactionList);
        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl("http://localhost:3000/job-payment/success/%d".formatted(paymentRequest.getJobId()));
        redirectUrls.setCancelUrl("http://localhost:9111/api/payments/cancel");
        redirectUrls.setReturnUrl("http://localhost:9111/api/payments/success");
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

    public JobTransaction executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        payment.execute(apiContext, paymentExecution);

        JobTransaction jobTransaction = jobTransactionRepository.findByPaypalOrderId(paymentId)
                .orElseThrow();
        jobTransaction.setTransactionStatus(TransactionStatus.FINISHED);
        jobFeignClient.payJob(jobTransaction.getJobId());
        return jobTransactionRepository.save(jobTransaction);
    }

    @Transactional
    public PayoutBatch getPayout(PayoutRequest payoutRequest) throws PayPalRESTException {
        freelancerFeignClient.getFreelancerById(payoutRequest.getFreelancerId());
        FreelancerBalance freelancerBalance = getBalance(payoutRequest.getFreelancerId());
        if(freelancerBalance.getBalance().compareTo(BigDecimal.valueOf(payoutRequest.getTotal())) < 0) {
            throw new NotEnoughMoneyException();
        }
        createTransaction(payoutRequest);

        Payout payout = new Payout();
        PayoutSenderBatchHeader payoutSenderBatchHeader = new PayoutSenderBatchHeader();
        payoutSenderBatchHeader.setRecipientType("EMAIL");
        payoutSenderBatchHeader.setEmailSubject("Test payout");
        payoutSenderBatchHeader.setSenderBatchId("Test_sdk_" + RandomStringUtils.randomAlphanumeric(7));
        payout.setSenderBatchHeader(payoutSenderBatchHeader);
        payout.setItems(buildPayoutItems(payoutRequest));
        return payout.create(apiContext, null);
    }

    private void createTransaction(PayoutRequest payoutRequest)  {
        com.bsuir.entity.Transaction transaction = com.bsuir.entity.Transaction.builder()
                .createdAt(LocalDateTime.now())
                .amount(BigDecimal.valueOf(payoutRequest.getTotal()))
                .freelancerId(payoutRequest.getFreelancerId())
                .receiver(payoutRequest.getReceiver())
                .build();
        transactionRepository.save(transaction);
    }

    private List<PayoutItem> buildPayoutItems(PayoutRequest payoutRequest) {
        PayoutItem payoutItem = new PayoutItem();
        payoutItem.setRecipientType("EMAIL");
        payoutItem.setReceiver(payoutRequest.getReceiver());

        Currency currency = new Currency(payoutRequest.getCurrency(), String.format(Locale.forLanguageTag(payoutRequest.getCurrency()), "%.2f", payoutRequest.getTotal()));
        payoutItem.setAmount(currency);
        payoutItem.setNote("Any payout");
        payoutItem.setSenderItemId("item_" + System.currentTimeMillis());
        return List.of(payoutItem);
    }

    public FreelancerBalance getBalance(String freelancerId) {
        List<JobTransaction> transactions = jobTransactionRepository.findAllByFreelancerIdAndTransactionStatus(freelancerId, TransactionStatus.FINISHED);
        List<com.bsuir.entity.Transaction> withdrawTransactions = transactionRepository.findAllByFreelancerId(freelancerId);

        BigDecimal balance = transactions.stream()
                .map(JobTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal withdrawBalance = withdrawTransactions.stream()
                .map(com.bsuir.entity.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FreelancerBalance(balance.subtract(withdrawBalance));
    }

    public List<TransactionHistory> getTransactionHistory(String freelancerId) {
        List<JobTransaction> transactions = jobTransactionRepository.findAllByFreelancerIdAndTransactionStatus(freelancerId, TransactionStatus.FINISHED);
        List<com.bsuir.entity.Transaction> withdrawTransactions = transactionRepository.findAllByFreelancerId(freelancerId);

        List<TransactionHistory> transactionHistories = new ArrayList<>();
        transactionHistories.addAll(transactions.stream()
                .map(transaction -> TransactionHistory.builder()
                        .jobId(transaction.getJobId())
                        .freelancerId(freelancerId)
                        .amount(transaction.getAmount())
                        .createdAt(transaction.getCreatedAt())
                        .status(TransactionHistoryStatus.TOP_UP)
                        .build()
                )
                .toList());
        transactionHistories.addAll(withdrawTransactions.stream()
                .map(transaction -> TransactionHistory.builder()
                        .jobId(null)
                        .freelancerId(freelancerId)
                        .amount(transaction.getAmount())
                        .createdAt(transaction.getCreatedAt())
                        .status(TransactionHistoryStatus.WITHDRAW)
                        .build()
                )
                .toList());

        transactionHistories.sort(Comparator.comparing(TransactionHistory::getCreatedAt).reversed());
        return transactionHistories;
    }

    public Long cancelPayment(String paymentId, String payerId) {
        JobTransaction jobTransaction = jobTransactionRepository.findByPaypalOrderId(paymentId)
                .orElseThrow();
        jobTransaction.setTransactionStatus(TransactionStatus.REJECTED);
        jobTransactionRepository.save(jobTransaction);
        return jobTransaction.getId();
    }

    public List<JobTransaction> getCustomerTransactionHistory(String customerId) {
        return jobTransactionRepository.findAllByCustomerId(customerId);
    }
}