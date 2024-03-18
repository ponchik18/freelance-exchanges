package com.bsuir.controller;

import org.keycloak.KeycloakSecurityContext;
import com.bsuir.dto.UserCreateRequest;
import com.bsuir.service.KeycloakService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.security.Principal;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class KeycloakController {

    private final KeycloakService keycloakService;

    @GetMapping
    public UserRepresentation getUserById(Principal principal) {
        String username = principal.getName();
        return keycloakService.getUserByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRepresentation createUser(@RequestBody UserCreateRequest userCreateRequest) {
        return keycloakService.createUser(userCreateRequest);
    }
}