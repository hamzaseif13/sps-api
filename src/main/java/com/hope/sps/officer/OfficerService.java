package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.OfficerRegisterRequest;
import com.hope.sps.dto.RegisterRequestMapper;
import com.hope.sps.dto.ScheduleMapper;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.util.RegistrationUtil;
import com.hope.sps.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficerService {

    private final RegisterRequestMapper registerRequestMapper;

    private final ScheduleMapper scheduleMapper;

    private final OfficerDTOMapper officerDTOMapper;

    private final OfficerRepository officerRepository;

    private final RegistrationUtil registrationUtil;


    public Long registerOfficer(OfficerRegisterRequest request) {

        UserDetailsImpl userDetails = registerRequestMapper.apply(request);

        registrationUtil.throwExceptionIfEmailExists(userDetails.getEmail());
        registrationUtil.throwExceptionIfPasswordInvalid(userDetails.getPassword());

        Schedule officerSchedule = scheduleMapper.apply(request);

        Set<Zone> officerZones = getOfficeZonesFromReq(request);

        var officer = new Officer(userDetails, officerSchedule, officerZones);

        System.out.println("officer = " + officer);
        return officerRepository.save(officer).getId();
    }

    public List<OfficerDTO> getAll() {
        return officerRepository
                .findAll()
                .stream()
                .map(officerDTOMapper)
                .collect(Collectors.toList());
    }

    public void updateOfficer(OfficerUpdateRequest request, Long officerId) {

        Optional<Officer> oldOfficer = officerRepository.findById(officerId);

    }

    private Set<Zone> getOfficeZonesFromReq(OfficerRegisterRequest request) {
        return request
                .getZoneIds()
                .stream()
                .map(Zone::new)
                .collect(Collectors.toSet());
    }
}
