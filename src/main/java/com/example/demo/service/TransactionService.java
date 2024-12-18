package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionType;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionService {

    Transaction create(TransactionType type, String reference, BigDecimal amount, String currency);

    Transaction toSuccess(long id);

    Transaction toError(long id);

    Optional<Transaction> find(long id);

    default Transaction get(final long id) {
        return find(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));
    }
}
