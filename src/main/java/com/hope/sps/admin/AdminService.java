package com.hope.sps.admin;

import com.hope.sps.UserInformation.Role;
import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.common.RegisterRequest;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.util.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<AdminDTO> getAllAdmins() {

        final List<AdminDTO> adminDTOS = new ArrayList<>();

        adminRepository.findAll().forEach(admin -> {
            adminDTOS.add(mapper.map(admin, AdminDTO.class));
        });

        return adminDTOS;
    }

    public Long registerAdmin(final RegisterRequest request) {
        if (adminRepository.existsByUserInformationEmail(request.getEmail()))
            throw new DuplicateResourceException("email already exists");

        final var adminDetails = mapper.map(request, UserInformation.class);
        adminDetails.setRole(Role.ADMIN);

        if (!request.getPassword().matches(Validator.passwordValidationRegex))
            throw new InvalidResourceProvidedException("invalid password");

        adminDetails.setPassword(passwordEncoder.encode(request.getPassword()));
        return adminRepository.save(new Admin(adminDetails)).getId();
    }

    public void deleteAdminById(final Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new ResourceNotFoundException("could not delete admin with id: {%s}, no admin found".formatted(adminId));
        }

        adminRepository.deleteById(adminId);
    }
}
