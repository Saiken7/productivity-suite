package com.productivity_suite.LifeCanvas.Responses;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class NotesResponse {
    private String id;
    private String title;
    private Map<String, Object> content;
}
