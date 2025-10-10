package com.productivity_suite.LifeCanvas.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductiveSessionRequest {
    private LocalDateTime startTime;
}
