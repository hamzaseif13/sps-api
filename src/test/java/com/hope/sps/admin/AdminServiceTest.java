package com.hope.sps.admin;

import com.hope.sps.common.RegisterRequest;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.util.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private AdminService adminService;


    @Test
    @DisplayName("testGetAllAdmins List of AdminDTO")
    public void testGetAllAdmins_shouldReturnListOfAdminDTO() {
        // Prepare
        final var adminEntities = new ArrayList<Admin>();
        final var testAdmin = new Admin(new UserInformation(1L, "John", "Doe", "john@example.com", "password", Role.ADMIN));
        final var testAdminDTO = new AdminDTO(1L, "John", "Doe", "john@example.com");
        adminEntities.add(testAdmin);

        when(adminRepository.findAll()).thenReturn(adminEntities);
        when(modelMapper.map(any(Admin.class), eq(AdminDTO.class))).thenReturn(testAdminDTO);

        // Execute
        final List<AdminDTO> result = adminService.getAllAdmins();

        // Verify
        assertThat(result.get(0)).isEqualTo(testAdminDTO);
        verify(adminRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(Admin.class), eq(AdminDTO.class));
    }

    @Test
    @DisplayName("testRegisterAdmin duplicate email")
    public void testRegisterAdmin_duplicateEmail_shouldThrowDuplicateResourceException() {
        // Prepare
        final var request = new RegisterRequest("John", "Doe", "john@example.com", "password");
        when(adminRepository.existsByUserInformationEmail(request.getEmail())).thenReturn(true);

        // Execute and Verify
        assertThrows(DuplicateResourceException.class, () -> adminService.registerAdmin(request));
        verify(adminRepository, times(1)).existsByUserInformationEmail(request.getEmail());
        verifyNoMoreInteractions(adminRepository);
    }

    @Test
    @DisplayName("testRegisterAdmin invalid password")
    public void testRegisterAdmin_invalidPassword_shouldThrowInvalidResourceProvidedException() {
        // Prepare
        final var request = new RegisterRequest("John", "Doe", "john@example.com", "short");
        when(adminRepository.existsByUserInformationEmail(request.getEmail())).thenReturn(false);

        // Execute and Verify
        assertThrows(InvalidResourceProvidedException.class, () -> adminService.registerAdmin(request));
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("testRegisterAdmin Admin Id")
    public void testRegisterAdmin_validRegisterRequest_shouldSuccessAndReturnAdminId() {
        // Prepare
        final var request = new RegisterRequest("John", "Doe", "john@example.com", "password");
        final var adminDetails = new UserInformation(1L, "John", "Doe", "john@example.com", "password", null);

        when(adminRepository.existsByUserInformationEmail(request.getEmail())).thenReturn(false);
        when(validator.validateUserPassword(anyString())).thenReturn(true);
        when(modelMapper.map(request, UserInformation.class)).thenReturn(adminDetails);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(new Admin(1L, adminDetails));

        // Execute
        final Long adminId = adminService.registerAdmin(request);

        // Verify
        assertThat(adminId).isGreaterThan(0);
        verify(modelMapper, times(1)).map(request, UserInformation.class);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    @DisplayName("testDeleteAdminById not found admin id")
    public void testDeleteAdminById_adminIdNotFound_shouldThrowResourceNotFoundException() {
        // Prepare
        final Long adminId = 1L;
        final String loggedInAdminEmail = "admin@example.com";
        when(adminRepository.existsById(adminId)).thenReturn(false);

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteAdminById(adminId, loggedInAdminEmail));
        verify(adminRepository, times(1)).existsById(adminId);
        verifyNoMoreInteractions(adminRepository);
    }

    @Test
    @DisplayName("testDeleteAdminById self deletion")
    public void testDeleteAdminById_selfDeletion_shouldThrowInvalidResourceProvidedException() {
        // Prepare
        final Long adminId = 1L;
        final String loggedInAdminEmail = "admin@example.com";
        final var loggedInAdmin = new Admin(adminId, new UserInformation(2L, "John", "Doe", loggedInAdminEmail, "password", Role.ADMIN));
        when(adminRepository.existsById(adminId)).thenReturn(true);
        when(adminRepository.findByUserInformationEmail(loggedInAdminEmail)).thenReturn(Optional.of(loggedInAdmin));

        // Execute and Verify
        assertThrows(InvalidResourceProvidedException.class, () -> adminService.deleteAdminById(adminId, loggedInAdminEmail));
        verify(adminRepository, times(1)).existsById(adminId);
        verify(adminRepository, times(1)).findByUserInformationEmail(loggedInAdminEmail);
        verifyNoMoreInteractions(adminRepository);
    }

    @Test
    @DisplayName("testDeleteAdminById self deletion")
    public void testDeleteAdminById() {
        // Prepare
        final Long adminId = 1L;
        final String loggedInAdminEmail = "admin@example.com";
        final var loggedInAdmin = new Admin(2L, new UserInformation(2L, "John", "Doe", loggedInAdminEmail, "password", Role.ADMIN));
        when(adminRepository.existsById(adminId)).thenReturn(true);
        when(adminRepository.findByUserInformationEmail(loggedInAdminEmail)).thenReturn(Optional.of(loggedInAdmin));

        // Execute
        adminService.deleteAdminById(adminId, loggedInAdminEmail);

        // Verify
        verify(adminRepository, times(1)).existsById(adminId);
        verify(adminRepository, times(1)).findByUserInformationEmail(loggedInAdminEmail);
        verify(adminRepository, times(1)).deleteById(adminId);
    }
}
