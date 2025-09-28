package com.productivity_suite.LifeCanvas.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(value = "sticky_notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StickyNotes {

    @Id
    private String id;
    private String userId;
    private String title;
    private String note;

    @CreatedDate
    private LocalDateTime createdAt;
}
