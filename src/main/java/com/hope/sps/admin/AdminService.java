package com.hope.sps.admin;

import com.hope.sps.common.RegisterRequest;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
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
        // list to contain mapped admin entities to adminDTO objects, needed by the controller
        final List<AdminDTO> adminDTOS = new ArrayList<>();

        // for each admin entity, map it to adminDTO object and add it to the list
        adminRepository.findAll()
                .forEach(admin ->
                        adminDTOS.add(mapper.map(admin, AdminDTO.class))
                );

        return adminDTOS;
    }

    @Transactional
    public Long registerAdmin(final RegisterRequest request) {
        // an email existing, similar to the one provided in the request?
        if (adminRepository.existsByUserInformationEmail(request.getEmail()))
            throw new DuplicateResourceException("email already exists");

        // not valid password according to validation policy?
        if (!request.getPassword().matches(Validator.passwordValidationRegex))
            throw new InvalidResourceProvidedException("invalid password");

        // here email and password are valid
        // map the RegisterRequest object to UserInformation Object and set role to ADMIN
        final var adminDetails = mapper.map(request, UserInformation.class);
        adminDetails.setRole(Role.ADMIN);

        // hash the adminDetail's password
        adminDetails.setPassword(passwordEncoder.encode(request.getPassword()));

        // save the admin into the database and return its id
        return adminRepository.save(new Admin(adminDetails)).getId();
    }

    @Transactional
    public void deleteAdminById(final Long adminId) {
        // invalid id provided ?
        if (!adminRepository.existsById(adminId)) {
            throw new ResourceNotFoundException("could not delete admin with id: {%s}, no admin found".formatted(adminId));
        }

        // delete the admin form the database
        adminRepository.deleteById(adminId);
    }
}
