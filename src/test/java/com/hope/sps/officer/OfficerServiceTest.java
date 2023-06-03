package com.hope.sps.officer;

import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.user_information.UserRepository;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatObject;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OfficerServiceTest {

    @Mock
    private OfficerRepository officerRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OfficerService underTest;

    private final ModelMapper mapper = new ModelMapper();

    private UserInformation testUserDetails;

    private Schedule testSchedule;

    private Set<Space> testSpaces;

    private Set<Zone> testZoneSet;

    private Officer testOfficer;

    private OfficerDTO testOfficerDTO;

    private OfficerRegisterRequest testOfficerRegistrationReq;


    @BeforeEach
    void setUp() {
        testUserDetails = new UserInformation(
                1L,
                "John@gmail.com",
                "ENCODED_PASSWORD",
                "John",
                "Doe",
                Role.OFFICER
        );

        testSchedule = new Schedule(
                1L,
                Time.valueOf("08:00:00"),
                Time.valueOf("18:00:00"),
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY)
        );

        testSpaces = Set.of(
                new Space(1L, 1, Space.State.AVAILABLE),
                new Space(2L, 2, Space.State.AVAILABLE)
        );

        testZoneSet = Set.of(new Zone(
                1L,
                "TC-1033",
                "city mall",
                1d, 2,
                Time.valueOf("7:0:0"),
                Time.valueOf("16:0:0"),
                testSpaces, new Zone.Location("amman-jo", 35d, 26d)
        ));

        testOfficer = new Officer(
                1L,
                "123321",
                testUserDetails,
                testSchedule,
                testZoneSet
        );

        testOfficerDTO = new OfficerDTO(
                testOfficer.getId(),
                testUserDetails.getFirstName(),
                testUserDetails.getLastName(),
                testUserDetails.getEmail(),
                testOfficer.getPhoneNumber(),
                testOfficer.getSchedule(),
                testOfficer.getZones()
        );

        testOfficerRegistrationReq = new OfficerRegisterRequest(
                "John",
                "Doe",
                "John@gmail.com",
                "John1234",
                Time.valueOf("8:0:0"),
                Time.valueOf("18:0:0"),
                List.of(DayOfWeek.FRIDAY.name(), DayOfWeek.MONDAY.name()),
                List.of(1L),
                "123321"
        );
    }


    @Test
    @DisplayName("test getALl()")
    void testGetAll_shouldReturnOfficerDTOs() {

        final List<OfficerDTO> officerDTOS = List.of(testOfficerDTO);

        Mockito.when(officerRepository.findAll())
                .thenReturn(List.of(testOfficer));

        final List<OfficerDTO> actualOfficerDTOS = underTest.getAll();

        assertThatObject(officerDTOS).isEqualTo(actualOfficerDTOS);
    }

    @Test
    @DisplayName("test registerOfficer(OfficerRegisterRequest request) valid email")
    void testRegisterOfficer_nonExistingEmail_shouldRegisterItAndReturnGeneratedId() {

        Mockito.when(userRepository.existsByEmail(testOfficerRegistrationReq.getEmail()))
                .thenReturn(false);

        Mockito.when(zoneRepository.findAllById(testOfficerRegistrationReq.getZoneIds()))
                .thenReturn(new ArrayList<>(testZoneSet));

        ArgumentCaptor<Officer> officerArgumentCaptor = ArgumentCaptor.forClass(Officer.class);

        Mockito.when(officerRepository.save(any()))
                .thenReturn(testOfficer);

        underTest.registerOfficer(testOfficerRegistrationReq);

        Mockito.verify(officerRepository)
                .save(officerArgumentCaptor.capture());

        final var actualOfficer = officerArgumentCaptor.getValue();
        assertThat(actualOfficer.getId()).isNull();
        assertThat(actualOfficer.getPhoneNumber()).isEqualTo(testOfficerRegistrationReq.getPhoneNumber());
        assertThat(actualOfficer.getSchedule().getDaysOfWeek()).isEqualTo(testSchedule.getDaysOfWeek());
        assertThat(actualOfficer.getSchedule().getStartsAt()).isEqualTo(testSchedule.getStartsAt());
        assertThat(actualOfficer.getSchedule().getEndsAt()).isEqualTo(testSchedule.getEndsAt());
        assertThat(actualOfficer.getZones()).isEqualTo(testZoneSet);
    }

    @Test
    @DisplayName("test registerOfficer(OfficerRegisterRequest request) invalid email")
    void testRegisterOfficer_existingEmail_shouldThrowDuplicateResourceException() {

        Mockito.when(userRepository.existsByEmail(testOfficerRegistrationReq.getEmail()))
                .thenReturn(true);

        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> underTest.registerOfficer(testOfficerRegistrationReq));
    }

    @Test
    void updateOfficer() {
        //todo
    }

    @Test
    @DisplayName("test getOfficerById(long officerId) exists")
    void testGetOfficerById_existsOfficer_shouldReturnOfficerDTO() {
        final Long officerId = 1L;

        Mockito.when(officerRepository.findById(officerId))
                .thenReturn(Optional.of(testOfficer));

        final OfficerDTO actual = underTest.getOfficerById(officerId);
        assertThatObject(actual).isEqualTo(testOfficerDTO);
    }

    @Test
    @DisplayName("test getOfficerById(long officerId) non exists")
    void testGetOfficerById_nonExistsOfficer_shouldThrowResourceNotFoundException() {
        final Long officerId = 1L;

        Mockito.when(officerRepository.findById(officerId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.getOfficerById(officerId));
    }

    @Test
    @DisplayName("test deleteOfficerById(long officerId) exists")
    void testDeleteOfficerById_existsOfficerId() {
        final Long officerId = 1L;

        Mockito.when(officerRepository.existsById(officerId))
                .thenReturn(true);

        underTest.deleteOfficerById(officerId);

        Mockito.verify(officerRepository)
                .deleteById(officerId);
    }

    @Test
    @DisplayName("test deleteOfficerById(long officerId) not exists")
    void testDeleteOfficerById_nonExistsOfficerId_shouldThrowResourceNotFoundException() {
        final Long officerId = 1L;

        Mockito.when(officerRepository.existsById(officerId))
                .thenReturn(false);

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.deleteOfficerById(officerId));
    }
}