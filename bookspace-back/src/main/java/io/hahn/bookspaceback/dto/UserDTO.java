package io.hahn.bookspaceback.dto;

import io.hahn.bookspaceback.entity.enums.Role;
import lombok.Data;

@Data
public class UserDTO {

    private String id;
    private String userName;
    private String email;
    private String password;
    private Role role;
}
