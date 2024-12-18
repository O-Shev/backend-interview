package com.example.demo.service;

import com.example.demo.event.NewTransactionEvent;
import com.example.demo.exception.DuplicateReferenceException;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.model.Balance;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.model.TransactionType;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.entity.TransactionEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultTransactionService implements TransactionService {

    private final BalanceService balanceService;
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Transaction create(
            final TransactionType type,
            final String reference,
            final BigDecimal amount,
            final String currency
    ) {
        if (transactionRepository.existsByReference(reference)) {
            throw new DuplicateReferenceException("Transaction with reference '" + reference + "' already exists.");
        }

        // Fetch or create the balance
        Balance balance = balanceService.getOrCreate(currency);
        TransactionEntity transaction = null;

        try {
            // Create the transaction with PROCESSING status
            transaction = new TransactionEntity(
                    balance.getId(),
                    reference,
                    type,
                    amount,
                    currency
            );
            transaction.setStatus(TransactionStatus.PROCESSING);
            transaction = transactionRepository.save(transaction);

            // Perform type-specific operations
            if (type == TransactionType.WITHDRAWAL) {
                if (balance.getAmount().compareTo(amount) < 0) {
                    throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
                }
                balanceService.setBalanceAmount(balance, balance.getAmount().subtract(amount));
                balanceService.save(balance);

                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction = transactionRepository.save(transaction);
            }

            // If everything goes well, call toSuccess for DEPOSIT
            if (type == TransactionType.DEPOSIT) {
                toSuccess(transaction.getId());
            }


            eventPublisher.publishEvent(new NewTransactionEvent(
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getCurrency(),
                    transaction.getReference(),
                    transaction.getStatus()
            ));


            return transaction;

        } catch (Exception e) {
            // Handle rollback for WITHDRAWAL in case of errors
            if (type == TransactionType.WITHDRAWAL && transaction != null) {
                toError(transaction.getId());
            }
            throw e; // Rethrow the exception to trigger transaction rollback
        }
    }

    @Override
    @Transactional
    public Transaction toSuccess(long id) {
        TransactionEntity transaction = getTransactionOrThrow(id);

        if (transaction.getStatus().isFinal()) {
            throw new IllegalStateException("Cannot mark a final transaction as success.");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);

        // Handle balance update for DEPOSIT
        if (transaction.getType() == TransactionType.DEPOSIT) {
            Balance balance = balanceService.get(transaction.getBalanceId());
            balanceService.setBalanceAmount(balance, balance.getAmount().add(transaction.getAmount()));
            balanceService.save(balance);
        }

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction toError(long id) {
        TransactionEntity transaction = getTransactionOrThrow(id);

        if (transaction.getStatus().isFinal()) {
            throw new IllegalStateException("Cannot mark a final transaction as error.");
        }

        transaction.setStatus(TransactionStatus.ERROR);

        // Rollback balance for WITHDRAWAL
        if (transaction.getType() == TransactionType.WITHDRAWAL) {
            Balance balance = balanceService.get(transaction.getBalanceId());
            balanceService.setBalanceAmount(balance, balance.getAmount().add(transaction.getAmount()));
            balanceService.save(balance);
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> find(long id) {
        return transactionRepository.findById(id).map(x -> x);
    }

    private TransactionEntity getTransactionOrThrow(long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id '" + id + "' not found."));
    }
}
