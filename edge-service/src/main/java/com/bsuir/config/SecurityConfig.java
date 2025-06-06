package com.bsuir.config;//package com.bsuir.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
//
//@Configuration
//@EnableWebFluxSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final ReactiveClientRegistrationRepository registrationRepository;
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf()
//                .disable()
//                .authorizeExchange()
//                    .pathMatchers(HttpMethod.POST, "/api/users/**").permitAll()
//                    .pathMatchers(HttpMethod.GET, "/api/freelancers/**").permitAll()
//                    .pathMatchers(HttpMethod.GET, "/api/rating/**").permitAll()
//                .pathMatchers(HttpMethod.GET, "/api/customers/**").permitAll()
//                .anyExchange().authenticated()
//                .and()
//                .oauth2Login()
//                .and()
//                .logout()
//                .logoutSuccessHandler(oidcLogoutSuccessHandler());
//
//        return http.build();
//    }
//
//    @Bean
//    public ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
//        return new OidcClientInitiatedServerLogoutSuccessHandler(registrationRepository);
//    }
//
//
//}