package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.entity.Book;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDTO(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isFeatured", expression = "java(dto.getIsFeatured() != null ? dto.getIsFeatured() : false)")
    Book toEntity(BookDTO dto);


    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookFromDto(BookDTO dto, @MappingTarget Book book);
}
