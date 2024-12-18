package com.example.demo.listener;

import com.example.demo.event.NewTransactionEvent;
import com.example.demo.repository.TransactionDocumentRepository;
import com.example.demo.repository.document.TransactionDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionKafkaListener {

    private final TransactionDocumentRepository transactionDocumentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Listen for messages from 'transaction-topic'
    @KafkaListener(topics = "transaction-topic")
    public void consume(String eventStr) {
        NewTransactionEvent event = null;
        try {
            event = objectMapper.readValue(eventStr, NewTransactionEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Consuming transaction event: {}", event);

        // Convert Kafka event to MongoDB document
        TransactionDocument transactionDocument = new TransactionDocument(
                event.getTransactionId(),
                event.getReference(),
                event.getType(),
                event.getStatus(),
                event.getAmount(),
                event.getCurrency(),
                LocalDateTime.now(), // created_at timestamp
                null // updated_at will be null initially
        );

        // Save the document to MongoDB
        try {
            transactionDocumentRepository.save(transactionDocument);
            log.info("Transaction document saved to MongoDB: {}", transactionDocument);
        } catch (Exception e) {
            log.error("Error saving transaction document to MongoDB", e);
        }
    }
}

