package com.hope.sps.violation;

import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViolationControllerTest {

    @Mock
    private ViolationService violationService;

    @InjectMocks
    private ViolationController violationController;

    @Test
    void getAll_ReturnsListOfViolations() {
        // Prepare
        final var violationDTOList = new ArrayList<ViolationDTO>();
        violationDTOList.add(new ViolationDTO(1L, "ABC123", "Toyota", "Red", "Speeding", "http://example.com/image.jpg", null, null, null));
        when(violationService.getAllViolations()).thenReturn(violationDTOList);

        // Execute
        final ResponseEntity<List<ViolationDTO>> response = violationController.getAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(violationDTOList);
    }

    @Test
    void getViolationLoggedInOfficer_ReturnsListOfViolations() {
        // Prepare
        final var loggedInOfficer = new UserInformation(1L, "John", "Doe", "john@example.com", "password", Role.OFFICER);
        final var violationDTOList = new ArrayList<ViolationDTO>();
        violationDTOList.add(new ViolationDTO(1L, "ABC123", "Toyota", "Red", "Speeding", "http://example.com/image.jpg", null, null, null));
        when(violationService.getViolationsByOfficerEmail(loggedInOfficer.getEmail())).thenReturn(violationDTOList);

        // Execute
        final ResponseEntity<List<ViolationDTO>> response = violationController.getViolationLoggedInOfficer(loggedInOfficer);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(violationDTOList);
    }

    @Test
    void createViolation_WithValidRequestAndLoggedInOfficer_ReturnsSuccess() {
        // Prepare
        final var request = new ReportViolationRequest("ABC123", "Red", "Toyota", "Speeding", "imageBase64", "imageType", 1L);
        final var loggedInOfficer = new UserInformation(1L, "John", "Doe", "john@example.com", "password", Role.OFFICER);

        // Execute
        final ResponseEntity<Void> response = violationController.createViolation(request, loggedInOfficer);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
        verify(violationService, times(1)).createViolation(request, loggedInOfficer.getEmail());
    }
}
