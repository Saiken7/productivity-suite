package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KanbanTodoListRequest {

    @NotNull
    @Size(min = 1, max = 30, message = "Title Should be of atleast 30 charecters")
    private String title;

    @NotNull
    @Size(min = 1, max = 50, message = "Description should be of atleast 50 charecters")
    private String description;
}
