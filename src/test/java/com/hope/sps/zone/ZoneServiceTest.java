package com.hope.sps.zone;

import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceDTO;
import com.hope.sps.zone.space.SpaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.junit.Assert.assertTrue;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private SpaceRepository spaceRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ZoneService zoneService;

    @Test
    void testGetAll_ReturnsListOfZoneDTOs() {
        // Prepare
        final var zone1 = createZone(1L);
        final var zone2 = createZone(2L);
        final List<Zone> zones = List.of(zone1, zone2);

        final var space1 = createSpace(1L, true);
        final var space2 = createSpace(2L, false);
        final Set<Space> spaces1 = Set.of(space1);
        final Set<Space> spaces2 = Set.of(space2);
        zone1.setSpaces(spaces1);
        zone2.setSpaces(spaces2);

        final var spaceDTO1 = createSpaceDTO(space1.getId(), space1.isAvailable());
        final var spaceDTO2 = createSpaceDTO(space2.getId(), space2.isAvailable());

        final var zoneDTO1 = createZoneDTO(1L);
        final var zoneDTO2 = createZoneDTO(2L);
        zoneDTO1.setSpaceList(List.of(spaceDTO1));
        zoneDTO2.setSpaceList(List.of(spaceDTO2));

        final List<ZoneDTO> expectedDTOs = List.of(zoneDTO1, zoneDTO2);

        when(zoneRepository.findAll()).thenReturn(zones);
        when(modelMapper.map(zone1, ZoneDTO.class)).thenReturn(zoneDTO1);
        when(modelMapper.map(zone2, ZoneDTO.class)).thenReturn(zoneDTO2);
        when(modelMapper.map(space1, SpaceDTO.class)).thenReturn(spaceDTO1);
        when(modelMapper.map(space2, SpaceDTO.class)).thenReturn(spaceDTO2);

        // Act
        final List<ZoneDTO> result = zoneService.getAll();

        // Assert
        assertThat(result).isEqualTo(expectedDTOs);
        verify(zoneRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Zone.class), eq(ZoneDTO.class));
        verify(modelMapper, times(2)).map(any(Space.class), eq(SpaceDTO.class));
    }

    @Test
    void testGetZoneById_ZoneExists_ReturnsZoneDTO() {
        // Prepare
        final Long zoneId = 1L;
        final var zone = createZone(zoneId);
        final var space = createSpace(1L, true);
        zone.setSpaces(Set.of(space));
        final var expectedDTO = createZoneDTO(zoneId);
        final var spaceDTO = createSpaceDTO(space.getId(), space.isAvailable());
        expectedDTO.setSpaceList(List.of(spaceDTO));

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));
        when(modelMapper.map(zone, ZoneDTO.class)).thenReturn(expectedDTO);
        when(modelMapper.map(space, SpaceDTO.class)).thenReturn(spaceDTO);

        // Execute
        final ZoneDTO result = zoneService.getZoneById(zoneId);

        // Assert
        assertThat(result).isEqualTo(expectedDTO);
        verify(zoneRepository, times(1)).findById(zoneId);
        verify(modelMapper, times(1)).map(zone, ZoneDTO.class);
        verify(modelMapper, times(1)).map(any(Space.class), eq(SpaceDTO.class));
    }

    @Test
    void testGetZoneById_ZoneDoesNotExist_ThrowsResourceNotFoundException() {
        // Prepare
        final Long zoneId = 1L;
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());

        // Execute & Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> zoneService.getZoneById(zoneId));

        verify(zoneRepository, times(1)).findById(zoneId);
        verifyNoMoreInteractions(modelMapper);
    }

    @Test
    void testRegisterZone_ValidRequest_ReturnsZoneId() {
        // Prepare
        final var request = createZoneRegistrationRequest();
        final var zone = getZoneFromRegistrationRequest(request);
        when(zoneRepository.existsByTag(request.getTag())).thenReturn(false);
        when(modelMapper.map(request, Zone.class)).thenReturn(zone);
        when(zoneRepository.save(zone)).thenReturn(zone);
        zone.setId(1L);

        // Execute
        final Long result = zoneService.registerZone(request);

        // Assert
        assertThat(result).isEqualTo(1L);
        assertThat(zone.getSpaces().size()).isEqualTo(1);
        verify(zoneRepository, times(1)).existsByTag(request.getTag());
        verify(modelMapper, times(1)).map(request, Zone.class);
        verify(zoneRepository, times(1)).save(zone);
    }

    @Test
    void testRegisterZone_ZoneWithSameTagExists_ThrowsDuplicateResourceException() {
        // Prepare
        final var request = createZoneRegistrationRequest();
        when(zoneRepository.existsByTag(request.getTag())).thenReturn(true);

        // Execute & Assert
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> zoneService.registerZone(request));

        verify(zoneRepository, times(1)).existsByTag(request.getTag());
        verifyNoMoreInteractions(modelMapper, zoneRepository, spaceRepository);
    }

    @Test
    void testUpdateZone_ValidRequestWithoutSpaceUpdate_UpdatesZoneSuccessfully() {
        // Prepare
        final Long zoneId = 1L;
        final var request = createZoneUpdateRequest();
        request.setNumberOfSpaces(0);
        final var zone = getZoneFromUpdateRequest(request);
        zone.setNumberOfSpaces(0);

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));

        // Execute
        assertDoesNotThrow(() -> zoneService.updateZone(zoneId, request));

        // Assert
        assertThat(zone.getFee()).isEqualTo(request.getFee());
        assertThat(zone.getTitle()).isEqualTo(request.getTitle());
        assertThat(zone.getStartsAt()).isEqualTo(request.getStartsAt());
        assertThat(zone.getEndsAt()).isEqualTo(request.getEndsAt());
        verify(zoneRepository, times(1)).findById(zoneId);
        verify(zoneRepository, times(1)).save(zone);
        verifyNoInteractions(spaceRepository);
    }

    @Test
    void testUpdateZone_ValidRequestWithSpaceUpdate_UpdatesZoneSuccessfully() {
        // Prepare
        final Long zoneId = 1L;
        final var request = createZoneUpdateRequest();
        request.setNumberOfSpaces(4);
        final var zone = getZoneFromUpdateRequest(request);
        zone.setNumberOfSpaces(2);

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));

        // Execute
        assertDoesNotThrow(() -> zoneService.updateZone(zoneId, request));

        // Assert
        assertThat(zone.getFee()).isEqualTo(request.getFee());
        assertThat(zone.getTitle()).isEqualTo(request.getTitle());
        assertThat(zone.getStartsAt()).isEqualTo(request.getStartsAt());
        assertThat(zone.getEndsAt()).isEqualTo(request.getEndsAt());
        assertThat(zone.getNumberOfSpaces()).isEqualTo(4);
        assertThat(zone.getSpaces().size()).isEqualTo(4);
        verify(zoneRepository, times(1)).findById(zoneId);
        verify(spaceRepository, times(1)).removeAllByZoneId(zoneId);
        verify(zoneRepository, times(1)).save(zone);
    }

    @Test
    void testUpdateZone_InvalidActiveTime_ThrowsInvalidResourceProvidedException() {
        // Prepare
        final Long zoneId = 1L;
        final var request = createZoneUpdateRequest();
        final var zone = getZoneFromUpdateRequest(request);
        request.setStartsAt(Time.valueOf("10:0:0"));
        request.setEndsAt(Time.valueOf("9:0:0"));

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));

        // Execute & Assert
        assertThatExceptionOfType(InvalidResourceProvidedException.class)
                .isThrownBy(() -> zoneService.updateZone(zoneId, request));

        verify(zoneRepository, times(1)).findById(zoneId);
        verifyNoMoreInteractions(spaceRepository);
        verifyNoInteractions(spaceRepository);
    }

    @Test
    void testRemoveZone_ZoneExists_RemovesZoneSuccessfully() {
        // Arrange
        final Long zoneId = 1L;
        when(zoneRepository.existsById(zoneId)).thenReturn(true);

        // Execute
        assertDoesNotThrow(() -> zoneService.removeZone(zoneId));

        // Assert
        verify(zoneRepository, times(1)).existsById(zoneId);
        verify(zoneRepository, times(1)).deleteById(zoneId);
        verifyNoMoreInteractions(spaceRepository);
    }

    @Test
    void testRemoveZone_ZoneDoesNotExist_ThrowsResourceNotFoundException() {
        // Arrange
        final Long zoneId = 1L;
        when(zoneRepository.existsById(zoneId)).thenReturn(false);

        // Execute & Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> zoneService.removeZone(zoneId));

        verify(zoneRepository, times(1)).existsById(zoneId);
        verifyNoMoreInteractions(spaceRepository, zoneRepository);
    }

    private Zone createZone(Long id) {
        final var zone = new Zone();
        zone.setId(id);
        return zone;
    }

    private Space createSpace(Long id, boolean available) {
        final Space.State state = available ? Space.State.AVAILABLE : Space.State.TAKEN;
        return new Space(id, 1, state);
    }

    private SpaceDTO createSpaceDTO(Long id, boolean available) {
        final Space.State state = available ? Space.State.AVAILABLE : Space.State.TAKEN;
        return new SpaceDTO(id, 1, state);
    }

    private ZoneDTO createZoneDTO(Long id) {
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(id);
        return zoneDTO;
    }

    private ZoneRegistrationRequest createZoneRegistrationRequest() {
        ZoneRegistrationRequest request = new ZoneRegistrationRequest();
        request.setTag("tag");
        request.setTitle("title");
        request.setFee(10.0);
        request.setAddress("address");
        request.setLng(0.0);
        request.setLat(0.0);
        request.setNumberOfSpaces(1);
        request.setStartsAt(Time.valueOf("09:00:00"));
        request.setEndsAt(Time.valueOf("18:00:00"));
        return request;
    }

    private ZoneUpdateRequest createZoneUpdateRequest() {
        ZoneUpdateRequest request = new ZoneUpdateRequest();
        request.setTitle("title");
        request.setFee(10.0);
        request.setNumberOfSpaces(1);
        request.setStartsAt(Time.valueOf("09:00:00"));
        request.setEndsAt(Time.valueOf("18:00:00"));
        return request;
    }

    private Zone getZoneFromRegistrationRequest(ZoneRegistrationRequest request) {
        return new Zone(
                null,
                request.getTag(),
                request.getTitle(),
                request.getFee(),
                request.getNumberOfSpaces(),
                request.getStartsAt(),
                request.getEndsAt(),
                null,
                new Zone.Location(request.getAddress(), request.getLng(), request.getLat())
        );
    }

    private Zone getZoneFromUpdateRequest(ZoneUpdateRequest request) {
        return new Zone(
                null,
                null,
                request.getTitle(),
                request.getFee(),
                request.getNumberOfSpaces(),
                request.getStartsAt(),
                request.getEndsAt(),
                null,
                null
        );
    }
}
