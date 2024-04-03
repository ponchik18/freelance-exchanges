package com.bsuir.service;

import com.bsuir.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;


}