package com.hope.sps.officer.schedule;

import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @Test
    void getLoggedInOfficerSchedule_WithExistingSchedule_ReturnsScheduleDTO() {
        // Prepare
        final var loggedInOfficer = new UserInformation(1L, "John", "Doe", "john@example.com", "password", Role.OFFICER);
        final var scheduleDTO = new ScheduleDTO(Time.valueOf("08:00:00"), Time.valueOf("16:00:00"), new HashSet<>(), new HashSet<>());
        when(scheduleService.getCurrentScheduleByEmail(loggedInOfficer.getEmail())).thenReturn(Optional.of(scheduleDTO));

        // Execute
        final ResponseEntity<ScheduleDTO> response = scheduleController.getLoggedInOfficerSchedule(loggedInOfficer);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(scheduleDTO);
    }

    @Test
    void getLoggedInOfficerSchedule_WithMissingSchedule_ReturnsNoContent() {
        // Arrange
        final var loggedInOfficer = new UserInformation(1L, "John", "Doe", "john@example.com", "password", Role.OFFICER);
        when(scheduleService.getCurrentScheduleByEmail(loggedInOfficer.getEmail())).thenReturn(Optional.empty());

        // Act
        final ResponseEntity<ScheduleDTO> response = scheduleController.getLoggedInOfficerSchedule(loggedInOfficer);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
