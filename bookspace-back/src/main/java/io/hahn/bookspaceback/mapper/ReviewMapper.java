package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.ReviewDTO;
import io.hahn.bookspaceback.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "username", source = "readingList.user.userName")
    @Mapping(target = "book", source = "readingList.book")
    @Mapping(target = "rating", source = "readingList.rating")
    ReviewDTO toDTO(Review review);
}
