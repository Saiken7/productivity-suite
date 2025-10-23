package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GoalRequest {

    @NotNull(message = "Title Should not be empty")
    @NotBlank(message = "Title Should not be Blank")
    @Size(min = 1, max = 40, message = "Title should be max 40 characters")
    private String goalTitle;

    @NotNull(message = "Please Select Date")
    private LocalDate startTime;

    @NotNull(message = "Please Select Date")
    private LocalDate endTime;
}
