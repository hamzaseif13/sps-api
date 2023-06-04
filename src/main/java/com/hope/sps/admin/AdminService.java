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

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<AdminDTO> getAllAdmins() {

        // for each admin entity, map it to adminDTO object
        return adminRepository.findAll()
                .stream()
                .map(admin -> mapper.map(admin, AdminDTO.class))
                .toList();
    }

    @Transactional
    public Long registerAdmin(final RegisterRequest request) {
        // an email existing, similar to the one provided in the request?
        throwExceptionIfExistingEmail(request.getEmail());

        // not valid password according to validation policy?
        Validator.validateUserPassword(request.getPassword());

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
    public void deleteAdminById(final Long adminId, final String loggedInAdminEmail) {
        // invalid id provided ?
        throwExceptionIfAdminIdNotExisting(adminId);

        // get logged in admin
        final Admin loggedInAdmin = getLoggedInAdmin(loggedInAdminEmail);

        // is admin trying to delete him self?
        if (loggedInAdmin.getId().equals(adminId))
            throw new InvalidResourceProvidedException("an logged in admin cannot delete him self");

        // delete the admin form the database
        adminRepository.deleteById(adminId);
    }

    /************** HELPER_METHODS *************/

    private void throwExceptionIfExistingEmail(final String adminEmail) {
        if (adminRepository.existsByUserInformationEmail(adminEmail))
            throw new DuplicateResourceException("email already exists");
    }

    private void throwExceptionIfAdminIdNotExisting(final Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new ResourceNotFoundException(
                    "could not delete admin with id: {%s}, no admin found".formatted(adminId)
            );
        }
    }

    // will never throw exception, here the admin is authenticated and authorized
    private Admin getLoggedInAdmin(final String loggedInAdminEmail) {
        return adminRepository.findByUserInformationEmail(loggedInAdminEmail).orElseThrow();
    }
}
