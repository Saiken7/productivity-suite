package com.productivity_suite.LifeCanvas.Responses;

import com.productivity_suite.LifeCanvas.Entity.GoalsEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GoalsResponse {
    private String goalTitle;
    private LocalDate startTime;
    private LocalDate endTime;
    private GoalsEntity.Status status;
}
