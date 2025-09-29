package com.productivity_suite.LifeCanvas.Entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Size(min = 1,max = 30, message = "Title Cannot Exceed 30 characters")
    @NotBlank(message = "Title should not be blank")
    private String title;

    @Size(min = 1,max = 200, message = "Note Cannot Exceed 200 characters")
    @NotBlank(message = "Note should not be blank")
    private String note;

    @CreatedDate
    private LocalDateTime createdAt;
}
