package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.ReadingList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReadingListRequestMapper {

    @Mapping(target = "userID", ignore = true)
    @Mapping(target = "bookID", ignore = true)
    ReadingList toEntity(ReadingListRequestDTO dto);

    @Mapping(target = "userID", ignore = true)
    @Mapping(target = "bookID", ignore = true)
    void updateReadingListFromDTO(ReadingListRequestDTO dto, @MappingTarget ReadingList readingList);
}
