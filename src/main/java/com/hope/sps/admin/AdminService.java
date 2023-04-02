package com.hope.sps.admin;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminService {


    private final PasswordEncoder passwordEncoder;

    private final AdminRepository adminRepository;


    public Long registerAdmin(RegisterRequest request) {

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

    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream().map(this::fromAdmin).collect(Collectors.toList());
    }

    private AdminDto fromAdmin(Admin admin) {
        return new AdminDto(admin.getId(), admin.getUserDetails().getFirstName(),
                admin.getUserDetails().getLastName(), admin.getUserDetails().getEmail());
    }

    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }
}
