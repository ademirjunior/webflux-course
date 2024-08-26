package com.springreactive.webfluxcourse.mapper;

import com.springreactive.webfluxcourse.entity.User;
import com.springreactive.webfluxcourse.model.request.UserRequest;
import com.springreactive.webfluxcourse.model.response.UserResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);

    UserResponse toResponse(final User entity);

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest userRequest, @MappingTarget final User entity);
}
