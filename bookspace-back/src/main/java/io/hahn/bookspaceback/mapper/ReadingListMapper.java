package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.entity.ReadingList;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReadingListMapper {

    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "book", source = "book")
    ReadingListDTO toDTO(ReadingList book);


    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    void updateReadingListFromDto(ReadingListDTO dto, @MappingTarget ReadingList book);
}
