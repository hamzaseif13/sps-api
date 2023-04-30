package com.hope.sps.admin;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EmployeeRegisterRequestMapper implements Function<RegisterRequest, UserDetailsImpl> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsImpl apply(final RegisterRequest request) {

        return UserDetailsImpl.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
    }
}
