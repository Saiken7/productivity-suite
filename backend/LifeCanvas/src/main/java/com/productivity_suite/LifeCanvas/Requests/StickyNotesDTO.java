package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StickyNotesDTO {
    @Size(min = 1,max = 30, message = "Title Cannot Exceed 30 characters")
    @NotBlank(message = "Title should not be Blank")
    private String title;

    @Size(min = 1,max = 200, message = "Note cannot Exceed 200 Characters")
    @NotBlank(message = "Note should not be blank")
    private String note;
}
