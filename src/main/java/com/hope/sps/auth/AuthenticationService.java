package com.hope.sps.auth;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.UserDetails.UserRepository;
import com.hope.sps.admin.Admin;
import com.hope.sps.admin.AdminRepository;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.jwt.JwtUtils;
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
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail()
                        , request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("usrr not foiund"));
        var jwtToken = jwtUtils.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var userDetails = UserDetailsImpl.
                builder().
                firstName(request.getFirstName()).
                lastName(request.getLastName()).
                email(request.getEmail()).
                password(passwordEncoder.encode(request.getPassword())).
                role(Role.ADMIN).
                build();

        Admin admin = new Admin(userDetails);

        //customerRepository.save(customer);
        adminRepository.save(admin);
        var jwtToken = jwtUtils.generateToken(userDetails);
        return new AuthenticationResponse(jwtToken);
    }
}
