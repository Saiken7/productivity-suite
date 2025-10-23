package com.productivity_suite.LifeCanvas.Entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(value = "goals")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalsEntity {
    @Id
    private String id;
    private String userId;

    @NotNull(message = "Title Should not be empty")
    @NotBlank(message = "Title Should not be Blank")
    @Size(min = 1, max = 40, message = "Title should be max 40 characters")
    private String goalTitle;

    @NotNull(message = "Please Select Date")
    private LocalDate goalStartTime;
    @NotNull(message = "Please Select Date")
    private LocalDate goalEndTime;
    private Status status;

    public enum Status{
        IN_PROGRESS,
        COMPLETED
    }
}
