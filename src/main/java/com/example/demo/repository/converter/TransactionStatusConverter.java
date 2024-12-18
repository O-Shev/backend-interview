package com.example.demo.repository.converter;

import com.example.demo.model.TransactionStatus;
import jakarta.persistence.AttributeConverter;

public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, String> {
    public TransactionStatusConverter() {
    }

    @Override
    public String convertToDatabaseColumn(final TransactionStatus value) {
        return value != null ? value.name().toLowerCase() : null;
    }

    @Override
    public TransactionStatus convertToEntityAttribute(final String value) {
        return value != null ? TransactionStatus.valueOf(value.toUpperCase()) : null;
    }
}
