package com.hope.sps.admin;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.AssignScheduleRequest;
import com.hope.sps.dto.NewZoneRequest;
import com.hope.sps.dto.RegisterRequest;
import com.hope.sps.dto.UpdateZoneRequest;
import com.hope.sps.util.RegistrationUtil;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final RegistrationUtil registrationUtil;

    private final PasswordEncoder passwordEncoder;

    private final AdminRepository adminRepository;

    private final ZoneRepository zoneRepository;

    public Long registerAdmin(RegisterRequest request) {
        registrationUtil.throwExceptionIfEmailExists(request.getEmail());

        var adminDetails = getUserDetailsFromRegReq(request);

        return adminRepository.save(new Admin(adminDetails)).getId();
    }


    private UserDetailsImpl getUserDetailsFromRegReq(RegisterRequest request) {
        return UserDetailsImpl.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
    }
}
