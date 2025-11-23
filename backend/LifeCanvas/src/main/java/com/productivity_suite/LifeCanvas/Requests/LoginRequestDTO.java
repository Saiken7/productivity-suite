package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email should not be Blank")
    @Email(message = "Enter Valid Email")
    private String email;

    @NotBlank(message = "Password Field must not be blank")
    @Size(min = 6, message = "Password must be of atleast of 6 in length")
    private String password;
}
