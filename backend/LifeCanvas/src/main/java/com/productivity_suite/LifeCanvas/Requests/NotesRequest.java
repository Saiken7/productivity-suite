package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Builder
@Data
public class NotesRequest {

    @NotBlank(message = "Title should not be empty")
    private String title;

    @NotNull(message = "Write Something")
    private Map<String, Object> content;

}
