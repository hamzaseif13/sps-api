package com.hope.sps.officer;

import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.user_information.UserRepository;
import com.hope.sps.util.Validator;
import com.hope.sps.zone.ZoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Time;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficerServiceTest {

    @Mock
    private OfficerRepository officerRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator validator;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private OfficerService officerService;


    @Test
    void testGetAll_ShouldReturnListOfOfficerDTOs() {
        // Prepare
        final Officer officer1 = createMockOfficer(1L, "John", "Doe", "john@example.com");
        final Officer officer2 = createMockOfficer(2L, "Jane", "Smith", "jane@example.com");

        final OfficerDTO officer1DTO = createMockOfficerDTO(1L, "John", "Doe", "john@example.com");
        final OfficerDTO officer2DTO = createMockOfficerDTO(2L, "Jane", "Smith", "jane@example.com");

        final List<Officer> officers = List.of(officer1, officer2);

        when(officerRepository.findAll()).thenReturn(officers);
        when(mapper.map(officer1, OfficerDTO.class)).thenReturn(officer1DTO);
        when(mapper.map(officer2, OfficerDTO.class)).thenReturn(officer2DTO);

        // Execute
        final List<OfficerDTO> result = officerService.getAll();

        // Assert
        assertThat(result.size()).isEqualTo(officers.size());
        assertThat(result.get(0).getFirstName()).isEqualTo(officer1DTO.getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(officer1DTO.getLastName());
        assertThat(result.get(0).getEmail()).isEqualTo(officer1DTO.getEmail());

        assertThat(result.get(1).getFirstName()).isEqualTo(officer2DTO.getFirstName());
        assertThat(result.get(1).getLastName()).isEqualTo(officer2DTO.getLastName());
        assertThat(result.get(1).getEmail()).isEqualTo(officer2DTO.getEmail());

        verify(officerRepository).findAll();
        verify(mapper, times(2)).map(any(Officer.class), eq(OfficerDTO.class));
    }

    @Test
    void testGetOfficerById_WithExistingId_ShouldReturnOfficerDTO() {
        // Prepare
        final Long officerId = 1L;
        final Officer officer1 = createMockOfficer(officerId, "John", "Doe", "john@example.com");
        final OfficerDTO officer1DTO = createMockOfficerDTO(officerId, "John", "Doe", "john@example.com");

        when(officerRepository.findById(officerId)).thenReturn(Optional.of(officer1));
        when(mapper.map(officer1, OfficerDTO.class)).thenReturn(officer1DTO);

        // Execute
        final OfficerDTO result = officerService.getOfficerById(officerId);

        // Assert
        assertThat(result.getId()).isEqualTo(officerId);
        assertThat(result.getFirstName()).isEqualTo(officer1DTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(officer1DTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(officer1DTO.getEmail());

        verify(officerRepository).findById(officerId);
        verify(mapper, times(1)).map(any(Officer.class), eq(OfficerDTO.class));
    }

    @Test
    void testGetOfficerById_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Prepare
        final Long officerId = 1L;
        when(officerRepository.findById(officerId)).thenReturn(Optional.empty());

        // Execute & Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> officerService.getOfficerById(officerId));

        verify(officerRepository).findById(officerId);
        verifyNoInteractions(mapper);
    }

    @Test
    void testRegisterOfficer_WithValidRequest_ShouldRegisterOfficerAndReturnGeneratedId() {
        // Arrange
        final OfficerRegisterRequest request = createMockRegisterRequest();
        final var userInfo = new UserInformation(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), null);

        final var officer = createMockOfficer(1L, request.getFirstName(), request.getLastName(), request.getEmail());
        when(mapper.map(request, UserInformation.class)).thenReturn(userInfo);
        when(userRepository.existsByEmail(userInfo.getEmail())).thenReturn(false);
        when(validator.validateUserPassword(request.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(userInfo.getPassword())).thenReturn("hashedPassword");
        when(zoneRepository.findAllById(request.getZoneIds())).thenReturn(Collections.emptyList());
        when(officerRepository.save(any(Officer.class))).thenReturn(officer);

        // Act
        final Long result = officerService.registerOfficer(request);

        // Assert
        assertThat(result).isNotNull();

        verify(userRepository).existsByEmail(request.getEmail());
        verify(validator).validateUserPassword(request.getPassword());
        verify(passwordEncoder).encode(request.getPassword());
        verify(zoneRepository).findAllById(request.getZoneIds());
        verify(officerRepository).save(any(Officer.class));
    }

    @Test
    void testRegisterOfficer_WithDuplicateEmail_ShouldThrowDuplicateResourceException() {
        // Prepare
        final var request = createMockRegisterRequest();
        final var userInfo = new UserInformation(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), null);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        when(mapper.map(request, UserInformation.class)).thenReturn(userInfo);

        // Execute & Assert
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> officerService.registerOfficer(request));

        verify(userRepository).existsByEmail(request.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testRegisterOfficer_WithInvalidPassword_ShouldThrowInvalidResourceProvidedException() {
        // Prepare
        final var request = createMockRegisterRequest();
        final var userInfo = new UserInformation(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), null);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.map(request, UserInformation.class)).thenReturn(userInfo);
        when(validator.validateUserPassword(request.getPassword())).thenReturn(false);

        // Execute & Assert
        assertThatExceptionOfType(InvalidResourceProvidedException.class)
                .isThrownBy(() -> officerService.registerOfficer(request));

        // Verify that the validator method was called
        verify(validator).validateUserPassword(request.getPassword());
        verifyNoMoreInteractions(validator);
    }

    @Test
    void testUpdateOfficer_WithValidRequestAndExistingOfficer_ShouldUpdateOfficer() {
        // Prepare
        final Long officerId = 1L;
        final var request = createMockUpdateRequest();
        final var officer = createMockOfficer(officerId, "John", "Doe", "john@example.com");
        officer.setSchedule(new Schedule());

        when(officerRepository.findById(officerId)).thenReturn(Optional.of(officer));
        when(zoneRepository.findAllById(request.getZoneIds())).thenReturn(Collections.emptyList());
        when(officerRepository.save(any(Officer.class))).thenReturn(officer);

        // Execute
        officerService.updateOfficer(request, officerId);

        // Assert
        verify(officerRepository).findById(officerId);
        verify(zoneRepository).findAllById(request.getZoneIds());
        verify(officerRepository).save(any(Officer.class));
    }

    @Test
    void testUpdateOfficer_WithInvalidScheduleTime_ShouldThrowInvalidResourceProvidedException() {
        // Prepare
        final Long officerId = 1L;
        final var request = createMockUpdateRequest();
        request.setStartsAt(Time.valueOf("14:0:0"));
        request.setEndsAt(Time.valueOf("13:0:0"));

        // Execute & Assert
        assertThatExceptionOfType(InvalidResourceProvidedException.class)
                .isThrownBy(() -> officerService.updateOfficer(request, officerId));

        verifyNoInteractions(officerRepository);
        verifyNoInteractions(zoneRepository);
    }

    @Test
    void testUpdateOfficer_WithNonExistingOfficer_ShouldThrowResourceNotFoundException() {
        // Prepare
        final Long officerId = 1L;
        final var request = createMockUpdateRequest();
        when(officerRepository.findById(officerId)).thenReturn(Optional.empty());

        // Execute & Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> officerService.updateOfficer(request, officerId));

        verify(officerRepository).findById(officerId);
        verifyNoMoreInteractions(officerRepository);
    }

    @Test
    void testDeleteOfficerById_WithExistingOfficer_ShouldDeleteOfficer() {
        // Prepare
        final Long officerId = 1L;
        when(officerRepository.existsById(officerId)).thenReturn(true);

        // Execute
        officerService.deleteOfficerById(officerId);

        // Assert
        verify(officerRepository).existsById(officerId);
        verify(officerRepository).deleteById(officerId);
    }

    @Test
    void testDeleteOfficerById_WithNonExistingOfficer_ShouldThrowResourceNotFoundException() {
        // Prepare
        final Long officerId = 1L;
        when(officerRepository.existsById(officerId)).thenReturn(false);

        // Execute & Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> officerService.deleteOfficerById(officerId));

        verify(officerRepository).existsById(officerId);
        verifyNoMoreInteractions(officerRepository);
    }

    private OfficerRegisterRequest createMockRegisterRequest() {
        final var request = new OfficerRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        request.setStartsAt(Time.valueOf("8:0:0"));
        request.setEndsAt(Time.valueOf("16:0:0"));
        request.setDaysOfWeek(Collections.singletonList("MONDAY"));
        request.setZoneIds(Collections.singletonList(1L));
        request.setPhoneNumber("1234567890");
        return request;
    }

    private OfficerUpdateRequest createMockUpdateRequest() {
        final var request = new OfficerUpdateRequest();
        request.setStartsAt(Time.valueOf("9:0:0"));
        request.setEndsAt(Time.valueOf("17:0:0"));
        request.setDaysOfWeek(Collections.singletonList("TUESDAY"));
        request.setZoneIds(Collections.singletonList(2L));
        return request;
    }

    private Officer createMockOfficer(Long id, String firstName, String lastName, String email) {
        Officer officer = new Officer();
        officer.setId(id);
        UserInformation userInformation = new UserInformation();
        userInformation.setFirstName(firstName);
        userInformation.setLastName(lastName);
        userInformation.setEmail(email);
        officer.setUserInformation(userInformation);
        return officer;
    }

    private OfficerDTO createMockOfficerDTO(Long id, String firstName, String lastName, String email) {
        OfficerDTO officerDTO = new OfficerDTO();
        officerDTO.setId(id);
        officerDTO.setFirstName(firstName);
        officerDTO.setLastName(lastName);
        officerDTO.setEmail(email);

        return officerDTO;
    }
}
