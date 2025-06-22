package io.hahn.bookspaceback.dto;

import io.hahn.bookspaceback.entity.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDTO {
    private String username;
    private Role role;
    private String accessToken;
    private String refreshToken;
    // token expiration in ms
}
