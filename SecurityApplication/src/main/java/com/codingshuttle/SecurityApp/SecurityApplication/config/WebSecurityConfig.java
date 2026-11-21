package com.codingshuttle.SecurityApp.SecurityApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.codingshuttle.SecurityApp.SecurityApplication.filters.JwtAuthFilter;
import com.codingshuttle.SecurityApp.SecurityApplication.handlers.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws Exception {

        httpSecurity
            .authorizeHttpRequests(auth -> auth

                // Public APIs
                .requestMatchers(
                    "/error",
                    "/public/**",
                    "/auth/**",
                    "/home.html"
                ).permitAll()

                // Public health-check endpoint
                .requestMatchers(HttpMethod.GET, "/posts/isAlive")
                    .permitAll()

                // GET all posts: USER or ADMIN + POST_VIEW
                .requestMatchers(HttpMethod.GET, "/posts")
                    .access(new WebExpressionAuthorizationManager(
                        "hasAnyRole('USER', 'ADMIN') and hasAuthority('POST_VIEW')"
                    ))

                // GET individual post: POST_VIEW
                // Ownership will be checked at method level
                .requestMatchers(HttpMethod.GET, "/posts/**")
                    .hasAuthority(Permission.POST_VIEW.name())

                // Create post: ADMIN or CREATOR + POST_CREATE
                .requestMatchers(HttpMethod.POST, "/posts", "/posts/**")
                    .access(new WebExpressionAuthorizationManager(
                        "hasAnyRole('ADMIN', 'CREATOR') and hasAuthority('POST_CREATE')"
                    ))

                // Update post: ADMIN or CREATOR + POST_UPDATE
                .requestMatchers(HttpMethod.PUT, "/posts/**")
                    .access(new WebExpressionAuthorizationManager(
                        "hasAnyRole('ADMIN', 'CREATOR') and hasAuthority('POST_UPDATE')"
                    ))

                // Delete post: ADMIN + POST_DELETE
                .requestMatchers(HttpMethod.DELETE, "/posts/**")
                    .access(new WebExpressionAuthorizationManager(
                        "hasRole('ADMIN') and hasAuthority('POST_DELETE')"
                    ))

                .anyRequest().authenticated()
            )

            .csrf(csrfConfig -> csrfConfig.disable())

            .sessionManagement(sessionConfig ->
                sessionConfig.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .oauth2Login(oauth2Config -> oauth2Config
                .failureUrl("/login?error=true")
                .successHandler(oAuth2SuccessHandler)
            );

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
}