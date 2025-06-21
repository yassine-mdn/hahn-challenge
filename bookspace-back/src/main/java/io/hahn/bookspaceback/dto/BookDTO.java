package io.hahn.bookspaceback.dto;

import io.hahn.bookspaceback.entity.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private List<Genre> genres;
    private String description;
    private String coverUrl;
    private Boolean isFeatured;
    // add average rating
}
