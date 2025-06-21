package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User book);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO dto);


    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDTO dto, @MappingTarget User book);
}
