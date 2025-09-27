package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignupRequestDTO {

    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Enter Valid Email")
    private String email;
    @NotBlank(message = "Email should not be empty")
    private String phoneNumber;
    @NotBlank(message = "Password should not be empty")
    @Size(min = 6, message = "Password must be of atleast 6 charecters")
    private String password;
}
