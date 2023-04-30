package com.hope.sps.officer;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.space.Space;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatObject;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OfficerDTOMapperTest {

    private final OfficerDTOMapper mapper = new OfficerDTOMapper();

    @Test
    @DisplayName("test OfficerDTOMapper's apply(Officer officer)")
    void testApply() {
        final var testUserDetails = new UserDetailsImpl(
                1L,
                "John@gmail.com",
                "ENCODED_PASSWORD",
                "John",
                "Doe",
                Role.OFFICER
        );

        final var testSchedule = new Schedule(
                1L,
                Time.valueOf("08:00:00"),
                Time.valueOf("18:00:00"),
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY)
        );

        final var testSpaces = Set.of(
                new Space(1L, 1, Space.State.AVAILABLE),
                new Space(2L, 2, Space.State.AVAILABLE)
        );

        final Set<Zone> testZone = Set.of(
                new Zone(
                        1L,
                        "TC-1033",
                        "city mall",
                        1d, 2,
                        Time.valueOf("7:0:0"),
                        Time.valueOf("16:0:0"),
                        testSpaces, new Zone.Location("amman-jo", 35d, 26d)
                ));


        final var testOfficer = new Officer(
                testUserDetails,
                testSchedule,
                "123321",
                testZone
        );

        final OfficerDTO actualOfficerDTO = mapper.apply(testOfficer);

        assertThat(actualOfficerDTO.id()).isEqualTo(testOfficer.getId());
        assertThat(actualOfficerDTO.email()).isEqualTo(testUserDetails.getEmail());
        assertThat(actualOfficerDTO.firstName()).isEqualTo(testUserDetails.getFirstName());
        assertThat(actualOfficerDTO.lastName()).isEqualTo(testUserDetails.getLastName());
        assertThatObject(actualOfficerDTO.schedule()).isEqualTo(testSchedule);
        assertThatObject(actualOfficerDTO.zones()).isEqualTo(testZone);
        assertThat(actualOfficerDTO.phone()).isEqualTo(testOfficer.getPhone());
    }
}