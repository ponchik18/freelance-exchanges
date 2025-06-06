package com.bsuir.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


public class BearerTokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        final String authorization = HttpHeaders.AUTHORIZATION;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
            System.out.println("Bearer " + tokenValue);
            if (tokenValue == null || !tokenValue.trim().isEmpty()) {
                return;
            }
            requestTemplate.header(authorization, "Bearer " + tokenValue);
        }
    }
}