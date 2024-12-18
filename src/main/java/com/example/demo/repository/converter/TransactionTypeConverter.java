package com.example.demo.repository.converter;

import com.example.demo.model.TransactionType;
import jakarta.persistence.AttributeConverter;

public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {
    public TransactionTypeConverter() {
    }

    @Override
    public String convertToDatabaseColumn(final TransactionType value) {
        return value != null ? value.name().toLowerCase() : null;
    }

    @Override
    public TransactionType convertToEntityAttribute(final String value) {
        return value != null ? TransactionType.valueOf(value.toUpperCase()) : null;
    }
}
