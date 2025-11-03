package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalendarEventsRequest {
    @NotBlank(message = "Title Should not be Empty")
    private String title;
    @NotNull(message = "Mention the timeline")
    private boolean allDay;
    @NotNull(message = "Enter Start Date and Time")
    private LocalDateTime start;

    private LocalDateTime end;
}
