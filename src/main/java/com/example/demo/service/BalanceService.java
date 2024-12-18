package com.example.demo.service;

import com.example.demo.model.Balance;

import java.math.BigDecimal;

public interface BalanceService {

    Balance getOrCreate(String currency);

    void save(Balance balance);

    Balance setBalanceAmount(Balance balance, BigDecimal amount);

    Balance get(long balanceId);
}
