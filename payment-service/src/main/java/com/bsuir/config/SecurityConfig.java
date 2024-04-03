package com.bsuir.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(req -> {
                    try {
                        req.requestMatchers("/actuator/health/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/proposals").permitAll()
                                .requestMatchers(HttpMethod.GET, "/proposals/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/skills").permitAll()
                                .requestMatchers(HttpMethod.GET, "/skills/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jobs").permitAll()
                                .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()
                                .anyRequest()
                                .authenticated()
                                .and()
                                .oauth2ResourceServer()
                                .jwt()
                                .jwtAuthenticationConverter(jwtAuthConverter);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> {
            web.ignoring().requestMatchers(
                    HttpMethod.POST,
                    "/public/**",
                    "/customers"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.GET,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.DELETE,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.PUT,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                            HttpMethod.OPTIONS,
                            "/**"
                    )
                    .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
                            "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**");

        };
    }
}