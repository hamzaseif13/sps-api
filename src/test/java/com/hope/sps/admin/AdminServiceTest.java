package com.hope.sps.admin;

import com.hope.sps.common.RegisterRequest;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService underTest;

    private RegisterRequest testRegisterRequest;

    private UserInformation testUserInformation;

    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        testRegisterRequest = new RegisterRequest(
                "John",
                "Doe",
                "John@gmail.com",
                "John@1234"
        );

        testUserInformation = new UserInformation(
                "John@gmail.com",
                "ENCODED_PASSWORD",
                "John",
                "Doe",
                Role.ADMIN
        );

        testAdmin = new Admin(1L, testUserInformation);
    }

    @Test
    @DisplayName("test registerAdmin(RegisterRequest request)")
    void testRegisterAdmin_shouldRegisterTheAdminAndReturnTheGeneratedID() {

        //Given
        final Admin toRegisterAdmin = new Admin(testUserInformation);

        //When
        Mockito.when(adminRepository.existsByUserInformationEmail(
                        testRegisterRequest.getEmail()))
                .thenReturn(false);

        Mockito.when(passwordEncoder.encode(testRegisterRequest.getPassword()))
                .thenReturn("ENCODED_PASSWORD");

        Mockito.when(mapper.map(testRegisterRequest, UserInformation.class))
                .thenReturn(testUserInformation);

        Mockito.when(adminRepository.save(toRegisterAdmin))
                .thenReturn(testAdmin);

        final Long generatedId = underTest.registerAdmin(testRegisterRequest);

        //Then
        Mockito.verify(adminRepository).save(toRegisterAdmin);

        assertThat(generatedId).isEqualTo(1L);
        assertThat(toRegisterAdmin.getUserInformation().getPassword()).isEqualTo("ENCODED_PASSWORD");
    }

    @Test
    @DisplayName("test registerAdmin(RegisterRequest request) existingEmail")
    void testRegisterAdmin_existingEmail_shouldThrowDuplicateResourceException() {

        //Given
        //When
        Mockito.when(adminRepository.existsByUserInformationEmail(
                        testRegisterRequest.getEmail()))
                .thenReturn(true);

        // then
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(()->underTest.registerAdmin(testRegisterRequest));
    }

    @Test
    @DisplayName("test registerAdmin(RegisterRequest request) invalidPassword")
    void testRegisterAdmin_invalidPassword_shouldThrowInvalidResourceProvidedException() {

        //Given
        //When
        testRegisterRequest.setPassword("invalid password");
        // then
        assertThatExceptionOfType(InvalidResourceProvidedException.class)
                .isThrownBy(()->underTest.registerAdmin(testRegisterRequest));
    }

    @Test
    @DisplayName("test getAllAdmins()")
    void testGetAllAdmins_shouldReturnListOfAdminDTOs() {
        final AdminDTO testAdminDTO = new AdminDTO(
                testAdmin.getId(),
                testAdmin.getUserInformation().getFirstName(),
                testAdmin.getUserInformation().getLastName(),
                testAdmin.getUserInformation().getEmail()
        );

        Mockito.when(adminRepository.findAll())
                .thenReturn(List.of(testAdmin));

        Mockito.when(mapper.map(testAdmin, AdminDTO.class))
                .thenReturn(testAdminDTO);

        final List<AdminDTO> actualAdminDTOS = underTest.getAllAdmins();

        Mockito.verify(adminRepository).findAll();

        assertThat(actualAdminDTOS.size()).isEqualTo(1);

        assertThatObject(actualAdminDTOS.get(0)).isEqualTo(testAdminDTO);
    }

    @Test
    @DisplayName("test deleteAdminById(Long id) valid id")
    void testDeleteAdminById_validAdminId() {
        final Long testAdminID = 1L;

        Mockito.when(adminRepository.existsById(any()))
                .thenReturn(true);

        underTest.deleteAdminById(testAdminID);

        Mockito.verify(adminRepository).deleteById(testAdminID);
    }

    @Test
    @DisplayName("test deleteAdminById(Long id) invalid id")
    void testDeleteAdminById_invalidAdminId() {
        final Long testAdminID = 1L;

        Mockito.when(adminRepository.existsById(any()))
                .thenReturn(false);

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(()-> underTest.deleteAdminById(testAdminID));
    }
}