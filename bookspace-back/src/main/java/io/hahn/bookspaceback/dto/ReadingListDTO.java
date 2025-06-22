package io.hahn.bookspaceback.dto;

import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class ReadingListDTO {

    private Long id;
    private String userName;
    private BookBarebonesDTO book;
    private LocalDateTime addedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Status status;

    @Min(1)
    @Max(5)
    private Integer rating;
}
