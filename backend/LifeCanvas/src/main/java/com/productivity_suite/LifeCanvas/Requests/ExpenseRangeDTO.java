package com.productivity_suite.LifeCanvas.Requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRangeDTO {

    @NotNull(message = "Start Date is Required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @NotNull(message = "End Date is Required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
}
