package com.takehome.stayease.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.takehome.stayease.entity.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotBlank(message = "Password must not be empty")
    // @Pattern(
    //     regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!_]).{8,}$",
    //     message = "Password must be at least 8 characters long, contain one uppercase letter,"
    //         + "one number, and one special character"
    // )
    @Size(min=8, message = "Password is required 8 character long")
    private String password;

    private Role role;
}
