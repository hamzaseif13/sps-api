package com.hope.sps.admin;

import com.hope.sps.UserInformation.Role;
import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.common.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatObject;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private EmployeeRegisterRequestMapper employeeRegisterRequestMapper;

    @Mock
    private AdminDTOMapper adminDTOMapper;

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
                "John1234"
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

        final Admin toRegisterAdmin = new Admin(testUserInformation);

        Mockito.when(employeeRegisterRequestMapper.apply(testRegisterRequest))
                .thenReturn(testUserInformation);

        Mockito.when(adminRepository.save(toRegisterAdmin))
                .thenReturn(testAdmin);

        final Long generatedId = underTest.registerAdmin(testRegisterRequest);

        Mockito.verify(employeeRegisterRequestMapper).apply(testRegisterRequest);

        Mockito.verify(adminRepository).save(toRegisterAdmin);

        assertThat(generatedId).isEqualTo(1L);
    }

    @Test
    @DisplayName("test getAllAdmins()")
    void testGetAllAdmins_shouldReturnListOfAdminDTOs() {
        final AdminDTO testAdminDTO = new AdminDTO(
                testAdmin.getId(),
                testAdmin.getUserDetails().getFirstName(),
                testAdmin.getUserDetails().getLastName(),
                testAdmin.getUserDetails().getEmail()
        );

        Mockito.when(adminRepository.findAll())
                .thenReturn(List.of(testAdmin));

        Mockito.when(adminDTOMapper.apply(testAdmin))
                .thenReturn(testAdminDTO);

        final List<AdminDTO> actualAdminDTOS = underTest.getAllAdmins();

        Mockito.verify(adminRepository).findAll();

        Mockito.verify(adminDTOMapper).apply(testAdmin);

        assertThat(actualAdminDTOS.size()).isEqualTo(1);

        assertThatObject(actualAdminDTOS.get(0)).isEqualTo(testAdminDTO);
    }

    @Test
    @DisplayName("test deleteAdminById(Long id)")
    void testDeleteAdminById() {
        final Long testAdminID = 1L;

        underTest.deleteAdminById(testAdminID);

        Mockito.verify(adminRepository).deleteById(testAdminID);
    }
}