package com.springreactive.webfluxcourse.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import com.springreactive.webfluxcourse.entity.User;
import com.springreactive.webfluxcourse.mapper.UserMapper;
import com.springreactive.webfluxcourse.model.request.UserRequest;
import com.springreactive.webfluxcourse.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @Test
    @DisplayName("Teste do endpoint save com suscesso")
    void testSaveWithSuccess() {
        final var request = new UserRequest("Ademir", "ademir@email.com", "Ad123456789@");
        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        verify(service, times(1)).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Teste do endpoint save com bad request")
    void testSaveWithBadRequest() {
        final var request = new UserRequest("Ademir", "ademir@email.com", "Ad56789@");

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.errorMessage").isEqualTo("Error on attributes validation")
                .jsonPath("$.errors[0].fieldName").isEqualTo("password");

    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}