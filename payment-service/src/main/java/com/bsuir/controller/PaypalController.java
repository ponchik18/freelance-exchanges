package com.bsuir.controller;

import com.bsuir.dto.freelancer.FreelancerBalance;
import com.bsuir.dto.link.RedirectLinks;
import com.bsuir.dto.payment.PaymentRequest;
import com.bsuir.dto.payment.PayoutRequest;
import com.bsuir.dto.transaction.TransactionHistory;
import com.bsuir.entity.JobTransaction;
import com.bsuir.entity.Transaction;
import com.bsuir.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;

    @PostMapping("/create-payment")
    public RedirectLinks createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        try {
            Payment payment = paypalService.createPayment(paymentRequest);
            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return RedirectLinks.builder()
                            .url(links.getHref())
                            .build();
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
        return RedirectLinks.builder()
                .url("http://localhost:3000/job-payment/cancel/%d".formatted(paymentRequest.getJobId()))
                .build();
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
    public void createPayout(@RequestBody @Valid PayoutRequest payoutRequest) {
        try {
            paypalService.getPayout(payoutRequest).getBatchHeader().getPayoutBatchId();
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/success")
    public RedirectView paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) throws PayPalRESTException {
        JobTransaction jobTransaction = paypalService.executePayment(paymentId, payerId);
        return new RedirectView("http://localhost:3000/job-payment/success/%d".formatted(jobTransaction.getJobId()));
    }

    @GetMapping(value = "/cancel", produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView paymentError(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        Long jobId = paypalService.cancelPayment(paymentId, payerId);
        return new RedirectView("http://localhost:3000/job-payment/cancel/%d".formatted(jobId));
    }

    @GetMapping("/balance/{freelancerId}")
    public FreelancerBalance getBalance(@PathVariable String freelancerId) {
        return paypalService.getBalance(freelancerId);
    }

    @GetMapping("/transaction/history/{freelancerId}")
    public List<TransactionHistory> getTransactionHistory(@PathVariable String freelancerId) {
        return paypalService.getTransactionHistory(freelancerId);
    }

    @GetMapping("/transaction/history/customer/{customerId}")
    public List<JobTransaction> getCustomerTransactionHistory(@PathVariable String customerId) {
        return paypalService.getCustomerTransactionHistory(customerId);
    }
}