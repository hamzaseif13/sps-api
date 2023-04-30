package com.hope.sps.admin;

import com.hope.sps.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final EmployeeRegisterRequestMapper employeeRegisterRequestMapper;

    private final AdminDTOMapper adminDTOMapper;

    private final PasswordEncoder passwordEncoder;

    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(adminDTOMapper)
                .toList();
    }

    public Long registerAdmin(final RegisterRequest request) {

        var adminDetails = employeeRegisterRequestMapper.apply(request);
        adminDetails.setPassword(passwordEncoder.encode(request.getPassword()));

        System.err.println(adminDetails);
        return adminRepository.save(new Admin(adminDetails)).getId();
    }

    public void deleteAdminById(final Long id) {
        adminRepository.deleteById(id);
    }
}
