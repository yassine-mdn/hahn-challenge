package io.hahn.bookspaceback.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    Long id;
    BookBarebonesDTO book;
    String username;
    String comment;
    Integer rating;
    LocalDateTime cratedAt;
}
