package io.hahn.bookspaceback.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Pattern(message = "Username must contain only alphanumeric characters and hyphens (-)", regexp = "^[a-zA-Z0-9-]+$")
    private String userName;
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
}
