package com.example.demo.repository.entity;

import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.model.TransactionType;
import com.example.demo.repository.converter.TransactionStatusConverter;
import com.example.demo.repository.converter.TransactionTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "transaction",
        uniqueConstraints = {
                @UniqueConstraint(name = "transaction_reference_unique", columnNames = {"reference"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TransactionEntity implements Transaction {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Column(name = "balance_id", nullable = false, updatable = false)
    private long balanceId;

    @Getter
    @Convert(converter = TransactionTypeConverter.class)
    @Column(name = "type", nullable = false, updatable = false)
    private TransactionType type;

    @Getter
    @Setter
    @Convert(converter = TransactionStatusConverter.class)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Getter
    @Column(name = "reference", length = 64, nullable = false, updatable = false)
    private String reference;

    @Getter
    @Column(name = "amount", precision = 27, scale = 18)
    private BigDecimal amount;

    @Getter
    @Column(name = "currency", nullable = false)
    private String currency;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TransactionEntity(
            final long balanceId,
            @NonNull final String reference,
            @NonNull final TransactionType type,
            @NonNull final BigDecimal amount,
            @NonNull final String currency
    ) {
        this.balanceId = balanceId;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = TransactionStatus.NEW;
    }
}
