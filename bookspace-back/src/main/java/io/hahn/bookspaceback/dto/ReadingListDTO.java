package io.hahn.bookspaceback.dto;

import io.hahn.bookspaceback.entity.enums.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadingListDTO {

    private Long id;
    private String username;
    private BookBarebonesDTO book;
    private LocalDateTime addedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Status status;

    @Min(1)
    @Max(5)
    private Integer rating;
}
