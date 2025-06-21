package io.hahn.bookspaceback.entity;

import io.hahn.bookspaceback.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
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

    @Enumerated(EnumType.ORDINAL)
    private Role role = Role.USER;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
