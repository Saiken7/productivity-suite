package com.productivity_suite.LifeCanvas.Responses;

import com.productivity_suite.LifeCanvas.Entity.KanbanTodoListEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KanbanTodoListResponse {
    private String id;
    private String title;
    private String description;
    private KanbanTodoListEntity.Status status;

}
