package com.productivity_suite.LifeCanvas.Requests;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExpenseTrackerDTO {
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
}
