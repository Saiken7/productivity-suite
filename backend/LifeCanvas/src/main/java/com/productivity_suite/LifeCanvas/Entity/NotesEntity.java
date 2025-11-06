package com.productivity_suite.LifeCanvas.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotesEntity {

    @Id
    private String id;

    private String userId;
    private String title;
    private Map<String, Object> content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
