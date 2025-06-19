package io.hahn.bookspaceback.entity;

import io.hahn.bookspaceback.entity.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;
    private String author;
    private String publisher;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private List<Genre> genres;
    private String description;
    private String coverUrl;
}
