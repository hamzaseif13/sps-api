package com.hope.sps.officer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfficerControllerTest {

    @Mock
    private OfficerService officerService;

    @InjectMocks
    private OfficerController officerController;

    @Test
    void getAll_ReturnsListOfOfficers() {
        // Prepare
        final var officerDTOList = new ArrayList<OfficerDTO>();
        officerDTOList.add(new OfficerDTO(1L, "John", "Doe", "john@example.com", "1234567890", null, null));
        when(officerService.getAll()).thenReturn(officerDTOList);

        // Execute
        final ResponseEntity<List<OfficerDTO>> response = officerController.getAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(officerDTOList);
    }

    @Test
    void getById_WithValidId_ReturnsOfficerDTO() {
        // Arrange
        final Long officerId = 1L;
        final var officerDTO = new OfficerDTO(officerId, "John", "Doe", "john@example.com", "1234567890", null, null);
        when(officerService.getOfficerById(officerId)).thenReturn(officerDTO);

        // Execute
        final ResponseEntity<OfficerDTO> response = officerController.getById(officerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(officerDTO);
    }

    @Test
    void register_WithValidRequest_ReturnsOfficerId() {
        // Prepare
        final var request = new OfficerRegisterRequest("John", "Doe", "john@example.com", "password", Time.valueOf("08:00:00"), Time.valueOf("16:00:00"), new ArrayList<>(), new ArrayList<>(), "1234567890");
        final Long officerId = 1L;
        when(officerService.registerOfficer(request)).thenReturn(officerId);

        // Execute
        final ResponseEntity<Long> response = officerController.register(request);

        // Assert
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(officerId);
    }

    @Test
    void updateOfficer_WithValidRequestAndId_ReturnsSuccessMessage() {
        // Prepare
        final var request = new OfficerUpdateRequest(Time.valueOf("08:00:00"), Time.valueOf("16:00:00"), new ArrayList<>(), new ArrayList<>());
        final Long officerId = 1L;

        // Execute
        final ResponseEntity<String> response = officerController.updateOfficer(request, officerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("officer updated");
    }

    @Test
    void deleteOfficerById_WithValidId_ReturnsNoContent() {
        // Prepare
        final Long officerId = 1L;

        // Execute
        final ResponseEntity<String> response = officerController.deleteOfficerById(officerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }
}
