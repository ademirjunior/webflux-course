package com.springreactive.webfluxcourse.model.response;

public record UserResponse(
        String id,
        String name,
        String email,
        String password
) { }
