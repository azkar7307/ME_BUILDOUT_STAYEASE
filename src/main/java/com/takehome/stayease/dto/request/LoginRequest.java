package com.takehome.stayease.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    // @Pattern(
    //     regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!_]).{8,}$",
    //     message = "Password must be at least 8 characters long, contain one uppercase letter,"
    //         + "one number, and one special character"
    // )
    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be 8 character long")
    private String password;
}
