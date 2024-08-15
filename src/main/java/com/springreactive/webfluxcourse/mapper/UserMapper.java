package com.springreactive.webfluxcourse.mapper;

import com.springreactive.webfluxcourse.entity.User;
import com.springreactive.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);
}
