package com.productivity_suite.LifeCanvas.Responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CalendarEventResponse {
    private String id;
    private String title;
    private boolean allDay;
    private LocalDateTime start;
    private LocalDateTime end;
}
