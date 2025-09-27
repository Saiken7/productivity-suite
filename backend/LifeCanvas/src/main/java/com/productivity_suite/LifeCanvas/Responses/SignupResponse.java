package com.productivity_suite.LifeCanvas.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponse {
    private String userId;
    private String name;
    private String email;
    private Boolean isAccountVerified;
}
