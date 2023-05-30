package com.hope.sps.violation;

import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViolationService {

    private final ViolationRepository violationRepository;

    private final OfficerRepository officerRepository;

    private final ModelMapper mapper;

    public void reportViolation(final ReportViolationRequest request, final String officerEmail) {

        final Officer loggedInOfficer = getLoggedInOfficer(officerEmail);

        final Violation violationToReport = mapper.map(request, Violation.class);
        violationToReport.setOfficer(loggedInOfficer);

        violationRepository.save(violationToReport);
    }

    private Officer getLoggedInOfficer(final String officerEmail) {
        return officerRepository.getOfficerByUserInformationEmail(officerEmail).orElseThrow();
    }
}
