package io.hahn.bookspaceback.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hahn.bookspaceback.entity.enums.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReadingListRequestDTO {

    // userId would be inferred from the logged-in user in the controller layer
    @JsonIgnore
    private String username;
    @NotNull
    private Long bookID;
    private Status status;

    @Min(1)
    @Max(5)
    private Integer rating;
}
