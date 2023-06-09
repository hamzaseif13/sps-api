package com.hope.sps.zone.space;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpaceControllerTest {

    @Mock
    private SpaceService spaceService;

    @InjectMocks
    private SpaceController spaceController;

    @Test
    void getOccupiedSpaceInformation_WithValidSpaceId_ReturnsOccupiedSpaceDTO() {
        // Prepare
        final Long spaceId = 1L;
        final var occupiedSpaceDTO = new OccupiedSpaceDTO("John Doe", "Toyota", "Red", LocalDateTime.now(), 60L, false);
        when(spaceService.getOccupiedSpaceInformation(spaceId)).thenReturn(occupiedSpaceDTO);

        // Execute
        final ResponseEntity<OccupiedSpaceDTO> response = spaceController.getOccupiedSpaceInformation(spaceId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(occupiedSpaceDTO);
    }

    @Test
    void checkSpaceAvailability_WithValidRequest_ReturnsSpaceAvailabilityResponse() {
        // Prepare
        final var request = new SpaceAvailabilityRequest(1L, 1);
        final var availabilityResponse = new SpaceAvailabilityResponse(true, "available");
        when(spaceService.checkAvailability(request)).thenReturn(availabilityResponse);

        // Execute
        final ResponseEntity<SpaceAvailabilityResponse> response = spaceController.checkSpaceAvailability(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(availabilityResponse);
    }
}
