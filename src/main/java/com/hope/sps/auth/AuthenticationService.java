package com.hope.sps.auth;

import com.hope.sps.customer.Customer;
import com.hope.sps.jwt.JwtUtils;
import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.UserDetails.UserRepository;
import com.hope.sps.officer.Officer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail()
                    ,request.getPassword()
            )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("usrr not foiund"));
        var jwtToken = jwtUtils.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = UserDetailsImpl.
                builder().
                email(request.getEmail()).
                password(passwordEncoder.encode(request.getPassword())).
                role(Role.ADMIN).
                build();
        userRepository.save(user);
        var jwtToken = jwtUtils.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
