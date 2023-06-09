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
    private String violationBucket;

    public List<ViolationDTO> getAllViolations() {
        return this.violationRepository.findAll()
                .stream()
                .map(violation -> mapper.map(violation, ViolationDTO.class))
                .toList();
    }

    public List<ViolationDTO> getViolationsByOfficerEmail(final String officerEmail) {
        return this.violationRepository
                .findByOfficerUserInformationEmail(officerEmail)
                .stream()
                .map(violation -> mapper.map(violation, ViolationDTO.class))
                .toList();
    }

    public void createViolation(final ReportViolationRequest request, final String officerEmail) {

        // get loggedInOfficer
        final Officer loggedInOfficer = getLoggedInOfficer(officerEmail);

        // get violation object from ReportViolationRequest object
        final Violation violationToReport = mapper.map(request, Violation.class);

        //assign the officer and the zone to the violation
        violationToReport.setOfficer(loggedInOfficer);
        violationToReport.setZone(new Zone(request.getZoneId()));

        // prepare violation image name
        final String random = UUID.randomUUID().toString();
        final String objectKey = "violation-" + random + "." + request.getImageType();

        // decode the actual image
        final byte[] imageBytes = Base64.getDecoder().decode(request.getImageBase64());

        // save the image on s3
        s3Service.putObject(
                violationBucket,
                objectKey,
                request.getImageType(),
                imageBytes
        );

        // update violation's image url
        violationToReport.setImageUrl(objectKey);

        violationRepository.save(violationToReport);
    }

    // .orElseThrow(); will never get executed
    private Officer getLoggedInOfficer(final String officerEmail) {
        return officerRepository.getOfficerByUserInformationEmail(officerEmail).orElseThrow();
    }
}
