package com.productivity_suite.LifeCanvas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expense_tracker", indexes = {
        @Index(name = "idx_user_id" , columnList = "user_id"),
        @Index(name = "idx_user_transaction_date", columnList = "user_id, transaction_date")
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseTrackerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Needed Edit here
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_expense_user"))
    @JsonIgnore
    @NotNull(message = "User is Required")
    private UserEntity user;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount should be positive")
    private BigDecimal amount;

    @Size(min = 1, max = 30, message = "Description cannot exceed 30 characters")
    @NotBlank(message = "There should be Description for an Expense!")
    private String description;

    @Column(name = "transaction_date", nullable = false)
    @NotNull(message = "Transaction Date is Compulsory")
    private LocalDate transactionDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

