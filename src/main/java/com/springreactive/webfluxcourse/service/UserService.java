package com.springreactive.webfluxcourse.service;

import com.springreactive.webfluxcourse.entity.User;
import com.springreactive.webfluxcourse.mapper.UserMapper;
import com.springreactive.webfluxcourse.model.request.UserRequest;
import com.springreactive.webfluxcourse.respository.UserRepository;
import com.springreactive.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    public Mono<User> findById(final String id) {
        return handleNotFound(repository.findById(id), id);
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest userRequest) {
        return findById(id)
                .map(entity -> mapper.toEntity(userRequest, entity))
                .flatMap(repository::save);
    }

    public Mono<User> delete(final String id) {
        return handleNotFound(repository.findAndRemove(id), id);

    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id) {
        return mono.switchIfEmpty(
                Mono.error(new ObjectNotFoundException(
                        String.format("Object not found. ID: %s, Type %s", id, User.class.getSimpleName()))));
    }
}
