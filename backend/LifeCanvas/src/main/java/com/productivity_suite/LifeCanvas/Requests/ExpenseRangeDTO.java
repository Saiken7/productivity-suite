package com.productivity_suite.LifeCanvas.Requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRange {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
}
