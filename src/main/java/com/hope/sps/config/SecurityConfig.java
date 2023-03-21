package com.hope.sps.config;

import com.hope.sps.jwt.JWTAuthFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
            csrf().
            disable().
            authorizeHttpRequests().
            requestMatchers("/api/admin/**").hasAuthority("ADMIN").
            requestMatchers("/api/customer/**").hasAuthority("CUSTOMER").
            requestMatchers("/api/auth/**").
            permitAll().
            anyRequest().
            authenticated().
            and().
            sessionManagement().
            sessionCreationPolicy(SessionCreationPolicy.STATELESS).
            and().
            authenticationProvider(authenticationProvider).
            addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
