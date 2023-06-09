package com.hope.sps.zone;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneControllerTest {

    @Mock
    private ZoneService zoneService;

    @InjectMocks
    private ZoneController zoneController;

    @Test
    void getAll_ReturnsListOfZones() {
        // Prepare
        final var zoneDTOList = new ArrayList<ZoneDTO>();
        zoneDTOList.add(new ZoneDTO(1L, "tag", "title", 10.0, "address", 1.0, 2.0, 5, new Time(0), new Time(1), 3L, null));
        when(zoneService.getAll()).thenReturn(zoneDTOList);

        // Execute
        final ResponseEntity<List<ZoneDTO>> response = zoneController.getAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(zoneDTOList);
    }

    @Test
    void getZoneById_ReturnsZone() {
        // Prepare
        final Long zoneId = 1L;
        final var zoneDTO = new ZoneDTO(zoneId, "tag", "title", 10.0, "address", 1.0, 2.0, 5, new Time(0), new Time(1), 3L, null);
        when(zoneService.getZoneById(zoneId)).thenReturn(zoneDTO);

        // Execute
        final ResponseEntity<ZoneDTO> response = zoneController.getZoneById(zoneId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(zoneDTO);
    }

    @Test
    void registerZone_WithValidRequest_ReturnsZoneId() {
        // Prepare
        final ZoneRegistrationRequest request = new ZoneRegistrationRequest("tag", "title", 10.0, "address", 1.0, 2.0, 5, new Time(0), new Time(1));

        // Execute
        final ResponseEntity<Long> response = zoneController.registerZone(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(zoneService, times(1)).registerZone(request);
    }

    @Test
    void updateZone_WithValidRequestAndZoneId_ReturnsZoneId() {
        // Prepare
        final var request = new ZoneUpdateRequest("title", 10.0, 5, new Time(0), new Time(1));
        final Long zoneId = 1L;

        // Execute
        final ResponseEntity<Long> response = zoneController.updateZone(request, zoneId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(zoneId);
        verify(zoneService, times(1)).updateZone(zoneId, request);
    }

    @Test
    void removeZone_WithValidZoneId_ReturnsNoContent() {
        // Prepare
        final Long zoneId = 1L;

        // Execute
        final ResponseEntity<Void> response = zoneController.removeZone(zoneId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(zoneService, times(1)).removeZone(zoneId);
    }
}
