package com.productivity_suite.LifeCanvas.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "calendarEvents")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarEventEntity {

    @Id
    private String id;
    private String userId;
    private String title;
    private boolean allDay;
    private LocalDateTime start;
    private LocalDateTime end;
    @CreatedDate
    private LocalDateTime createdAt;
}
