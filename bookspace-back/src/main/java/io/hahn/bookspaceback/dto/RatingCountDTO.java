package io.hahn.bookspaceback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingCountDTO {
    private Integer rating;
    private Long count;


}
