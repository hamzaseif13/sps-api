package com.hope.sps.violation;

import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerRepository;
import com.hope.sps.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationService {

    private final ViolationRepository violationRepository;

    private final OfficerRepository officerRepository;

    private final ModelMapper mapper;

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

    public void reportViolation(final ReportViolationRequest request, final String officerEmail) {

        final Officer loggedInOfficer = getLoggedInOfficer(officerEmail);

        final Violation violationToReport = mapper.map(request, Violation.class);
        violationToReport.setOfficer(loggedInOfficer);
        violationToReport.setZone(new Zone(request.getZoneId()));

        violationRepository.save(violationToReport);
    }

    private Officer getLoggedInOfficer(final String officerEmail) {
        return officerRepository.getOfficerByUserInformationEmail(officerEmail).orElseThrow();
    }
}
