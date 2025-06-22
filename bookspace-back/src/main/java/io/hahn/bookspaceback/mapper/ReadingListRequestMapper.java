package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.ReadingList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReadingListRequestMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReadingList toEntity(ReadingListRequestDTO dto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateReadingListFromDTO(ReadingListRequestDTO dto, @MappingTarget ReadingList readingList);
}
