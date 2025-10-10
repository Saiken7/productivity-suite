package com.productivity_suite.LifeCanvas.Responses;

import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductiveSessionResponse {
    private String sessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UserEntity user;
    private Long durationTime;
}
