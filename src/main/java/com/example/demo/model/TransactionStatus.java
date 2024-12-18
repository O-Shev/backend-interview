package com.example.demo.model;

public enum TransactionStatus {

    NEW(false),
    PROCESSING(false),
    SUCCESS(true),
    ERROR(true);

    private final boolean isFinal;

    TransactionStatus(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
