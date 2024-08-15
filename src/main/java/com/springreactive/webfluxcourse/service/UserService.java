package com.springreactive.webfluxcourse.service;

import com.springreactive.webfluxcourse.entity.User;
import com.springreactive.webfluxcourse.mapper.UserMapper;
import com.springreactive.webfluxcourse.model.request.UserRequest;
import com.springreactive.webfluxcourse.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    public Mono<User> save (final UserRequest request){
        return repository.save(mapper.toEntity(request));
    }
}
