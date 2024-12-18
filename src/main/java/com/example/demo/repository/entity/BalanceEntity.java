package com.example.demo.repository.entity;

import com.example.demo.model.Balance;
import jakarta.persistence.Column;
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
        name = "balance",
        uniqueConstraints = {
                @UniqueConstraint(name = "balance_currency_unique", columnNames = {"currency"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BalanceEntity implements Balance {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
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

    public BalanceEntity(
            @NonNull final String currency
    ) {
        this.amount = BigDecimal.ZERO;
        this.currency = currency;
    }
}
