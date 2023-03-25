package com.hope.sps.auth;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.admin.AdminRepository;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.jwt.JwtUtils;
import com.hope.sps.officer.OfficerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final AdminRepository adminRepository;

    private final OfficerRepository officerRepository;

    private final CustomerRepository customerRepository;

    public AuthenticationResponse authenticateAdmin(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        throwExceptionIfNonAdmin(userDetails);

        String token = jwtUtils.generateToken(userDetails);
        Long adminId = adminRepository.getAdminIdByUserDetailsId(userDetails.getId());

        return new AuthenticationResponse(adminId, token, userDetails.getRole());
    }

    public AuthenticationResponse authenticateOfficerAndCustomer(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        throwExceptionIfAdmin(userDetails);

        Role userRole = userDetails.getRole();
        Long userId;

        if (userRole.equals(Role.OFFICER))
            userId = officerRepository.getOfficerIdByUserDetailsId(userDetails.getId());
        else
            userId = customerRepository.getCustomerIdByUserDetailsId(userDetails.getId());

        String token = jwtUtils.generateToken(userDetails);

        return new AuthenticationResponse(userId, token, userRole);
    }

    private void throwExceptionIfNonAdmin(UserDetailsImpl userDetails) {
        boolean isAdmin = isAdminTryingToLogin(userDetails);

        if (!isAdmin)
            throw new BadCredentialsException("Officers and customers are not allowed to login here");
    }

    private void throwExceptionIfAdmin(UserDetailsImpl userDetails) {
        boolean isAdmin = isAdminTryingToLogin(userDetails);

        if (isAdmin)
            throw new BadCredentialsException("Admins are not allowed to login here");
    }

    private boolean isAdminTryingToLogin(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)//todo verify
                .anyMatch(
                        auth -> auth.equals(Role.ADMIN.toString())
                );
    }

}
