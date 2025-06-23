package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.ReviewDTO;
import io.hahn.bookspaceback.dto.ReviewRequestDTO;
import io.hahn.bookspaceback.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewRequestMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "readingList", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Review toEntity(ReviewRequestDTO dto);
}
