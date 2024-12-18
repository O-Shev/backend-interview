package com.example.demo.service;

import com.example.demo.exception.BalanceNotFoundException;
import com.example.demo.model.Balance;
import com.example.demo.repository.BalanceRepository;
import com.example.demo.repository.entity.BalanceEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultBalanceService implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public Balance getOrCreate(String currency) {
        return balanceRepository.findByCurrency(currency)
                .orElseGet(() -> balanceRepository.save(new BalanceEntity(currency)));
    }

    @Override
    public void save(Balance balance) {
        balanceRepository.save((BalanceEntity) balance);
    }

    @Override
    @Transactional
    public Balance setBalanceAmount(Balance balance, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance amount cannot be negative.");
        }

        BalanceEntity balanceEntity = (BalanceEntity) balance;
        balanceEntity.setAmount(amount);
        return balanceRepository.save(balanceEntity);
    }

    @Override
    public Balance get(long balanceId) {
        return balanceRepository.findById(balanceId)
                .orElseThrow(() -> new BalanceNotFoundException("Balance with id '" + balanceId + "' not found."));
    }
}
