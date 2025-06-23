package io.hahn.bookspaceback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.hahn.bookspaceback.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private String id;
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Pattern(message = "Username must contain only alphanumeric characters and hyphens (-)", regexp = "^[a-zA-Z0-9-]+$")
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role role;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
}
