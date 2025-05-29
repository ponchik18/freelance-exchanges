package com.bsuir.controller;

import com.bsuir.dto.UserCreateRequest;
import com.bsuir.dto.UserResponse;
import com.bsuir.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class KeycloakController {

    private final KeycloakService keycloakService;

    @GetMapping
    public UserResponse getUser(JwtAuthenticationToken authenticationToken) {
        String id = authenticationToken
                .getToken()
                .getClaimAsString("sub");
        return keycloakService.getUserByEmail(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRepresentation createUser(@RequestBody UserCreateRequest userCreateRequest) {
        return keycloakService.createUser(userCreateRequest);
    }
}