package com.hope.sps.violation;

import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerRepository;
import com.hope.sps.s3.S3Service;
import com.hope.sps.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ViolationService {

    private final ViolationRepository violationRepository;

    private final OfficerRepository officerRepository;

    private final ModelMapper mapper;
    private final S3Service s3Service;

    @Value("${aws.violationBucket}")
    private  String violationBucket;

    public List<ViolationDTO> getAllViolations() {
        return this.violationRepository.findAll()
                .stream()
                .map(violation -> mapper.map(violation, ViolationDTO.class))
                .toList();
    }

    public List<ViolationDTO> getViolationsByOfficerEmail(final String officerEmail) {

        final Officer loggedInOfficer = getLoggedInOfficer(officerEmail);

        return this.violationRepository.findByOfficerId(loggedInOfficer.getId())
                .stream()
                .map(violation -> mapper.map(violation, ViolationDTO.class))
                .toList();
    }


    private Officer getLoggedInOfficer(final String officerEmail) {
        return officerRepository.getOfficerByUserInformationEmail(officerEmail).orElseThrow();
    }


    public Violation createViolation(ReportViolationRequest request,String officerEmail) {
        final Officer loggedInOfficer = getLoggedInOfficer(officerEmail);

        final Violation violationToReport = mapper.map(request, Violation.class);
        violationToReport.setOfficer(loggedInOfficer);
        violationToReport.setZone(new Zone(request.getZoneId()));
        String random = UUID.randomUUID().toString();

        String objectKey = "violation-" + random + "." + request.getImageType();
        byte[] imageBytes  =  Base64.getDecoder().decode(request.getImageBase64());

        s3Service.putObject(
                violationBucket,
                objectKey,
                request.getImageType(),
                imageBytes
        );
        violationToReport.setImageUrl(objectKey);
        violationRepository.save(violationToReport);
        return violationToReport;
    }
}
