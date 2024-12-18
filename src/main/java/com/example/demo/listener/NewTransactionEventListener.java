package com.example.demo.listener;

import com.example.demo.event.NewTransactionEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewTransactionEventListener {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @EventListener
    public void handleTransactionEvent(NewTransactionEvent event) {
        log.info("Received transaction event: {}", event);

        // Send the event to the Kafka topic
        try {
            kafkaTemplate.send("transaction-topic", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Transaction event sent to Kafka topic: transaction-topic");
    }
}
