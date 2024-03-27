package com.bsuir.feign;

import com.bsuir.config.FeignClientConfiguration;
import com.bsuir.dto.freelancer.FreelancerResponse;
import com.bsuir.dto.user.UserRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "keycloak-service",
        configuration = FeignClientConfiguration.class,
        path = "/api/users"
)
public interface KeycloakFeignClient {
    @GetMapping
    UserRepresentation getUser();
}