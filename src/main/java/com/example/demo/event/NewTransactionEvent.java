package com.example.demo.event;

import com.example.demo.model.TransactionStatus;
import com.example.demo.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewTransactionEvent {

    private final long transactionId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String currency;
    private final String reference;
    private final TransactionStatus status;
}