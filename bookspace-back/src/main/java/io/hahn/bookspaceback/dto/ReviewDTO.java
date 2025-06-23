package io.hahn.bookspaceback.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    Long id;
    BookBarebonesDTO book;
    String username;
    String comment;
    Integer rating;
    LocalDateTime createdAt;
}
