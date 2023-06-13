package com.hope.sps.violation;

import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerDTO;
import com.hope.sps.officer.OfficerRepository;
import com.hope.sps.s3.S3Service;
import com.hope.sps.zone.Zone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViolationServiceTest {

    @Mock
    private ViolationRepository violationRepository;

    @Mock
    private OfficerRepository officerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private S3Service s3Service;

    @Value("${aws.violationBucket}")
    private String violationBucket;

    @InjectMocks
    private ViolationService violationService;

    @Test
    void testGetAllViolations_ShouldReturnListOfViolationDTOs() {
        // Prepare
        final var violation = new Violation(1L, "123456", "BMW", "RED", "Details", "img", LocalDateTime.now(), new Officer(), new Zone());
        final var violationDTO = new ViolationDTO(1L, "123456", "BMW", "RED", "Details", "img", LocalDateTime.now(), new OfficerDTO(), new Zone());
        final List<Violation> violations = List.of(violation);
        when(violationRepository.findAll()).thenReturn(violations);
        when(modelMapper.map(violation, ViolationDTO.class)).thenReturn(violationDTO);

        // Execute
        final List<ViolationDTO> result = violationService.getAllViolations();

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(violationDTO);
        verify(violationRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(violation, ViolationDTO.class);
    }

    @Test
    void testGetViolationsByOfficerEmail_WithValidEmail_ShouldReturnListOfViolationDTOs() {
        // Prepare
        final String officerEmail = "test@example.com";
        final var violation = new Violation(1L, "123456", "BMW", "RED", "Details", "img", LocalDateTime.now(), new Officer(), new Zone());
        final var violationDTO = new ViolationDTO(1L, "123456", "BMW", "RED", "Details", "img", LocalDateTime.now(), new OfficerDTO(), new Zone());
        final List<Violation> violations = List.of(violation);
        when(violationRepository.findByOfficerUserInformationEmail(officerEmail)).thenReturn(violations);
        when(modelMapper.map(violation, ViolationDTO.class)).thenReturn(violationDTO);

        // Execute
        final List<ViolationDTO> result = violationService.getViolationsByOfficerEmail(officerEmail);

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(violationDTO);
        verify(violationRepository, times(1)).findByOfficerUserInformationEmail(officerEmail);
        verify(modelMapper, times(1)).map(violation, ViolationDTO.class);
    }

    @Test
    void testCreateViolation_WithValidRequestAndOfficerEmail_ShouldSaveViolationAndUploadImageToS3() {
        // Prepare
        final String officerEmail = "test@example.com";
        final var officer = new Officer();
        final var violation = new Violation(1L, "123456", "BMW", "RED", "Details", "img", LocalDateTime.now(), new Officer(), new Zone());
        final String imageBase64 = "cGFzc3dvcmQ=";
        final byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
        final var request = new ReportViolationRequest(violation.getPlateNumber(), violation.getCarColor(), violation.getCarBrand(), violation.getDetails(), imageBase64, "png", 1L);

        when(officerRepository.getOfficerByUserInformationEmail(officerEmail)).thenReturn(Optional.of(officer));
        when(modelMapper.map(request, Violation.class)).thenReturn(violation);

        // Execute
        violationService.createViolation(request, officerEmail);

        // Assert
        assertThat(officer).isEqualTo(violation.getOfficer());

        assertEquals(officer, violation.getOfficer());
        assertThat(violation.getZone()).isNotNull();
        assertThat(violation.getImageUrl()).isNotNull();
        verify(officerRepository, times(1)).getOfficerByUserInformationEmail(officerEmail);
        verify(modelMapper, times(1)).map(request, Violation.class);
        verify(s3Service, times(1)).putObject(eq(violationBucket), anyString(), eq("png"), eq(imageBytes));
        verify(violationRepository, times(1)).save(violation);
    }
}
