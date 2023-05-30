package com.hope.sps.officer.schedule;

import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final OfficerRepository officerRepository;

    public Optional<ScheduleDTO> getCurrentScheduleByEmail(final String officerEmail) {
        final Officer loggedInOfficer = getOfficerByEmail(officerEmail);
        final Schedule officerSchedule = loggedInOfficer.getSchedule();

        if (officerSchedule == null)
            return Optional.empty();

        final ScheduleDTO scheduleDTO = new ScheduleDTO(officerSchedule.getStartsAt(), officerSchedule.getEndsAt(), loggedInOfficer.getZones());
        return Optional.of(scheduleDTO);
    }

    //********* HELPERS METHODS ************//
    // Get logged-in officer from db by email.
    // NOTE: will never return null
    private Officer getOfficerByEmail(final String officerEmail) {
        return officerRepository.getOfficerByUserInformationEmail(officerEmail).orElse(null);
    }
}
