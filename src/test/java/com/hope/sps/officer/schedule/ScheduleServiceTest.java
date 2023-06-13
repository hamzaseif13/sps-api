package com.hope.sps.officer.schedule;

import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private OfficerRepository officerRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void testGetCurrentScheduleByEmail_WithExistingSchedule_ShouldReturnScheduleDTO() {
        // Prepare
        final String officerEmail = "test@example.com";

        // Create a mock Officer with a schedule
        final var officer = new Officer();
        officer.setZones(Collections.emptySet());
        final var schedule = new Schedule();
        schedule.setStartsAt(Time.valueOf("09:00:00"));
        schedule.setEndsAt(Time.valueOf("17:00:00"));
        schedule.setDaysOfWeek(Collections.singleton(DayOfWeek.MONDAY));
        officer.setSchedule(schedule);

        when(officerRepository.getOfficerByUserInformationEmail(officerEmail)).thenReturn(Optional.of(officer));

        // Execute
        final Optional<ScheduleDTO> result = scheduleService.getCurrentScheduleByEmail(officerEmail);

        // Assert
        assertThat(result.isPresent()).isTrue();
        final var scheduleDTO = result.orElseThrow();
        assertEquals(schedule.getStartsAt(), scheduleDTO.getStartsAt());
        assertEquals(schedule.getEndsAt(), scheduleDTO.getEndsAt());
        assertEquals(officer.getZones(), scheduleDTO.getZones());
        assertEquals(schedule.getDaysOfWeek(), scheduleDTO.getDaysOfWeek());

        verify(officerRepository).getOfficerByUserInformationEmail(officerEmail);
    }

    @Test
    void testGetCurrentScheduleByEmail_WithNoSchedule_ShouldReturnEmptyOptional() {
        // Arrange
        final String officerEmail = "test@example.com";

        when(officerRepository.getOfficerByUserInformationEmail(officerEmail)).thenReturn(Optional.of(new Officer()));

        // Prepare
        final Optional<ScheduleDTO> result = scheduleService.getCurrentScheduleByEmail(officerEmail);

        // Assert
        assertThat(result.isPresent()).isFalse();
        verify(officerRepository).getOfficerByUserInformationEmail(officerEmail);
    }
}
