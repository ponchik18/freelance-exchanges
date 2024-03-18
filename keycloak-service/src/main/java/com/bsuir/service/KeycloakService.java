package com.bsuir.service;

import com.bsuir.dto.UserCreateRequest;
import com.bsuir.dto.customer.CustomerCreateRequest;
import com.bsuir.dto.freelancer.FreelancerRequest;
import com.bsuir.feign.FreelancerFeignClient;
import com.bsuir.webclient.CustomerWebClient;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;
    private final FreelancerFeignClient freelancerFeignClient;
    private final CustomerWebClient customerWebClient;
    @Value("${keycloak.realm}")
    private String realm;

    private static String getUserId(Response response) {
        URI location = response.getLocation();
        String[] segments = location.getPath().split("/");
        return segments[segments.length - 1];
    }

    private static UserRepresentation getUserRepresentation(UserCreateRequest userCreateRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userCreateRequest.getUserName());
        user.setEmail(userCreateRequest.getEmail());
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userCreateRequest.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        credentials.add(credentialRepresentation);
        user.setCredentials(credentials);
        return user;
    }

    public UserRepresentation getUserByUsername(String username) {
        return getUsersResource().search(username)
                .get(0);
    }

    public UserRepresentation createUser(UserCreateRequest userCreateRequest) {
        UserRepresentation user = getUserRepresentation(userCreateRequest);
        String userId = null;
        try {

            Response response = getUsersResource().create(user);
            if (response.getStatus() != 201) {
                return null;
            }
            userId = getUserId(response);
            updateRole(userCreateRequest, userId);
            return getUserById(userId);
        } catch (Exception exception) {
            rollbackUserCreation(userId);
            throw new RuntimeException(exception);
        }
    }

    private void updateRole(UserCreateRequest userCreateRequest, String userId) {
        RealmResource realmResource = keycloak.realm(realm);
        RoleRepresentation role = realmResource.roles().get(userCreateRequest.getRole())
                .toRepresentation();
        UserResource userResource = getUsersResource().get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(role));
        if (userCreateRequest.getRole().equals("FREELANCER")) {
            createFreelancer(userCreateRequest, userId);
        } else if (userCreateRequest.getRole().equals("CUSTOMER")) {
            createCustomer(userCreateRequest, userId);
        }
    }

    private void createFreelancer(UserCreateRequest userCreateRequest, String userId) {
        FreelancerRequest freelancerRequest = FreelancerRequest.builder()
                .email(userCreateRequest.getEmail())
                .profilePicture(userCreateRequest.getProfilePicture())
                .userId(userId)
                .firstName(userCreateRequest.getFirstName())
                .lastName(userCreateRequest.getLastName())
                .build();
        freelancerFeignClient.createFreelancer(freelancerRequest);
    }

    private void createCustomer(UserCreateRequest userCreateRequest, String userId) {
        CustomerCreateRequest customerCreateRequest = CustomerCreateRequest.builder()
                .email(userCreateRequest.getEmail())
                .profilePicture(userCreateRequest.getProfilePicture())
                .userId(userId)
                .firstName(userCreateRequest.getFirstName())
                .lastName(userCreateRequest.getLastName())
                .build();
        customerWebClient.createCustomer(customerCreateRequest);
    }

    private void rollbackUserCreation(String userId) {
        if (userId == null || userId.isEmpty()) {
            return;
        }
        getUsersResource().delete(userId);
    }

    private UserRepresentation getUserById(String userId) {
        return getUsersResource().get(userId)
                .toRepresentation();
    }

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }
}