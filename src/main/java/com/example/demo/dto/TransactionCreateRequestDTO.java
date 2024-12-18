package com.example.demo.dto;

import com.example.demo.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateRequestDTO {

    @JsonProperty(value = "type")
    @NotNull(message = "Transaction type is required.")
    private TransactionType type;

    @JsonProperty(value = "amount")
    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @JsonProperty(value = "currency")
    @NotNull(message = "Currency is required.")
    @Size(min = 3, max = 3, message = "Currency must be a valid ISO 4217 code.")
    private String currency;

    @JsonProperty(value = "reference")
    @NotBlank(message = "Reference is required.")
    @Size(max = 64, message = "Reference cannot exceed 64 characters.")
    private String reference;
}
