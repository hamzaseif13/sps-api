package com.hope.sps.admin;

import com.hope.sps.common.RegisterRequest;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Test
    @DisplayName("testGetAll AdminDTO + OK status code")
    public void testGetAll_notEmptyAdminDTOList_shouldReturnListOfAdminDTOAndOKStatusCode() {
        // Prepare
        final var adminDTOS = new ArrayList<AdminDTO>();
        adminDTOS.add(new AdminDTO(1L, "John", "Doe", "john@example.com"));
        when(adminService.getAllAdmins()).thenReturn(adminDTOS);

        // Execute
        final ResponseEntity<List<AdminDTO>> response = adminController.getAll();

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(adminDTOS);
    }

    @Test
    @DisplayName("testGetAll NO_CONTENT status code")
    public void testGetAll_emptyAdminDTOList_shouldReturnNoContentStatusCode() {
        // Prepare
        when(adminService.getAllAdmins()).thenReturn(new ArrayList<>());

        // Execute
        final ResponseEntity<List<AdminDTO>> response = adminController.getAll();

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("testRegister adminId + OK status code")
    public void testRegister_validRegistrationRequest() {
        // Prepare
        final var request = new RegisterRequest("John", "Doe", "john@example.com", "password");
        final Long adminId = 1L;
        when(adminService.registerAdmin(request)).thenReturn(adminId);

        // Execute
        final ResponseEntity<Long> response = adminController.register(request);

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(adminId);
    }

    @Test
    @DisplayName("testDeleteById NO_CONTENT status code")
    public void testDeleteById() {
        // Prepare
        final Long adminId = 1L;
        final var loggedInAdmin = new UserInformation();
        doNothing().when(adminService).deleteAdminById(adminId, loggedInAdmin.getEmail());

        // Execute
        final ResponseEntity<Void> response = adminController.deleteById(adminId, loggedInAdmin);

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(adminService, times(1)).deleteAdminById(adminId, loggedInAdmin.getEmail());
    }
}
