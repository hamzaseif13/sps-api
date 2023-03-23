package com.hope.sps.dto;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class RegisterRequestMapper implements Function<RegisterRequest, UserDetailsImpl> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsImpl apply(RegisterRequest registerRequest) {

        return UserDetailsImpl
                .builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.OFFICER)
                .build();
    }
}
