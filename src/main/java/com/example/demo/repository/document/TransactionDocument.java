package com.example.demo.repository.document;

import com.example.demo.model.TransactionStatus;
import com.example.demo.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// TODO use it
@Getter
@Document("transaction")
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(def = "{'reference': 1}", unique = true),
})
public class TransactionDocument {

    @Id
    private long id;

    @Field("reference")
    private String reference;

    @Field("type")
    private TransactionType type;

    @Field("status")
    private TransactionStatus status;

    @Field("amount")
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
