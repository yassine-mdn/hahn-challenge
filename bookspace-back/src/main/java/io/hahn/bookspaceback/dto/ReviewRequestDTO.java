package io.hahn.bookspaceback.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    Long readingListId;
    String comment;
}
