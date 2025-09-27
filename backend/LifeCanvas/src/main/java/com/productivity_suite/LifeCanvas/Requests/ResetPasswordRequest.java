package com.productivity_suite.LifeCanvas.Requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Email Should not be Empty")
    private String email;
    @NotBlank(message = "OTP is required")
    private String otp;
    @NotBlank(message = "Password should not be empty")
    private String newPassword;
}
