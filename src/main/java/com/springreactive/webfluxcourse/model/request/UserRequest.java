package com.springreactive.webfluxcourse.model.request;

import com.springreactive.webfluxcourse.utils.Password;
import com.springreactive.webfluxcourse.utils.TrimString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @Size(min = 3, max = 50, message = "must be between 3 and 50 characters")
        @NotBlank(message = "must not be null or empty")
        String name,

        @Email(message = "invalid email")
        @NotBlank(message = "must not be null or empty")
        @TrimString
        String email,

        @Password
        String password
) {
}