package com.productivity_suite.LifeCanvas.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "todolist")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KanbanTodoListEntity {

    @Id
    private String id;

    private String userId;
    private String title;
    private String description;
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum Status{
        TODO,
        IN_PROGRESS,
        COMPLETED
    }
}
