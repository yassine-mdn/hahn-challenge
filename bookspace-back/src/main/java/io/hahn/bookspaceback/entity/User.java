package io.hahn.bookspaceback.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(length = 15, nullable = false, updatable = false, unique = true)
    private String userName;

    @Email
    private String email;

    @Column(length = 20, nullable = false, updatable = true)
    private String password;
}
