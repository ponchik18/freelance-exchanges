package com.bsuir.service;

import com.bsuir.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
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
    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    public UserRepresentation getUserByUsername(String username) {
        return getUsersResource().search(username)
                .get(0);
    }

    public UserRepresentation createUser(UserCreateRequest userCreateRequest) {
        UserRepresentation user = getUserRepresentation(userCreateRequest);

        Response response = getUsersResource().create(user);
        RealmResource realmResource = keycloak.realm(realm);
        RoleRepresentation role = realmResource.roles().get(userCreateRequest.getRole())
                .toRepresentation();
        if(response.getStatus() == 201) {
            URI location = response.getLocation();
            if (location != null) {
                String[] segments = location.getPath().split("/");
                String userId = segments[segments.length - 1];
                UserResource userResource = getUsersResource().get(userId);
                userResource.roles().realmLevel().add(Collections.singletonList(role));
                return getUserById(userId);
            }
        }
        return null;
    }

    private UserRepresentation getUserById(String userId) {
        return getUsersResource().get(userId)
                .toRepresentation();
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

    private RoleRepresentation getRole(String role) {
        RolesResource rolesResource = getRolesResource();
        return rolesResource.get(role).toRepresentation();
    }

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    private RolesResource getRolesResource(){
        return  keycloak.realm(realm).roles();
    }
}