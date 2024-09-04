package com.springreactive.webfluxcourse.controller;

import com.springreactive.webfluxcourse.entity.User;
import com.springreactive.webfluxcourse.mapper.UserMapper;
import com.springreactive.webfluxcourse.model.request.UserRequest;
import com.springreactive.webfluxcourse.model.response.UserResponse;
import com.springreactive.webfluxcourse.service.UserService;
import com.springreactive.webfluxcourse.service.exception.ObjectNotFoundException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    private static final String URI_TEST = "/users";
    public static final String NAME = "Ademir";
    public static final String ID = "1";
    public static final String EMAIL = "ademir@email.com";
    public static final String BAD_PASSWORD = "Ad56789@";
    public static final String PASSWORD = "Ad123456789@";
    public static final UserRequest USER_REQUEST = new UserRequest(NAME, EMAIL, PASSWORD);
    public static final UserResponse USER_RESPONSE = new UserResponse(ID, NAME, EMAIL, PASSWORD);

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @Test
    @DisplayName("Teste do endpoint save com suscesso")
    void testSaveWithSuccess() {

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri(URI_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(USER_REQUEST))
                .exchange()
                .expectStatus().isCreated();

        verify(service, times(1)).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Teste do endpoint save com bad request")
    void testSaveWithBadRequest() {
        final var request = new UserRequest(NAME, EMAIL, BAD_PASSWORD);

        webTestClient.post().uri(URI_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(URI_TEST)
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.errorMessage").isEqualTo("Error on attributes validation")
                .jsonPath("$.errors[0].fieldName").isEqualTo("password");

    }

    @Test
    @DisplayName("Test find by id with success")
    void testFindByIdWithSuccess() {

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(USER_RESPONSE);

        webTestClient.get().uri(URI_TEST + "/" + ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service).findById(anyString());
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test handle not found")
    void testHandleNotFound() {
        when(service.findById(anyString())).thenThrow(new ObjectNotFoundException("Object not found. ID: 2, Type User"));
        webTestClient.get().uri(URI_TEST + "/" + "2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(URI_TEST+ "/" + "2")
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.error").isEqualTo("Not Found")
                .jsonPath("$.errorMessage").isEqualTo("Object not found. ID: 2, Type User");

    }

    @Test
    @DisplayName("Test find all with success")
    void findAllWithSuccess() {

        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(USER_RESPONSE);

        webTestClient.get().uri(URI_TEST)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(ID)
                .jsonPath("$.[0].name").isEqualTo(NAME)
                .jsonPath("$.[0].email").isEqualTo(EMAIL)
                .jsonPath("$.[0].password").isEqualTo(PASSWORD);

        verify(service).findAll();
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test update with success")
    void update() {
        when(service.update( anyString(), any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(USER_RESPONSE);

        webTestClient.patch().uri(URI_TEST + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(USER_REQUEST))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(ID)
                .jsonPath("$.name").isEqualTo(NAME)
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service).update(anyString(),any(UserRequest.class));
        verify(mapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test delete with success")
    void deleteWithSuccess() {
        when(service.delete(anyString())).thenReturn(Mono.just(User.builder().build()));

        webTestClient.delete().uri(URI_TEST + "/" + ID)
                .exchange()
                .expectStatus().isOk();

        verify(service).delete(anyString());
    }
}