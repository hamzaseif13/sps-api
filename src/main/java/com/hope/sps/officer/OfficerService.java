package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.OfficerRegisterRequest;
import com.hope.sps.dto.RegisterRequestMapper;
import com.hope.sps.dto.ScheduleMapper;
import com.hope.sps.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    public Long registerOfficer(OfficerRegisterRequest request) {

        UserDetailsImpl userDetails = registerRequestMapper.apply(request);

        Schedule officerSchedule = scheduleMapper.apply(request);

        Set<Zone> officerZones = request
                .getZoneIds()
                .stream()
                .map(Zone::new)
                .collect(Collectors.toSet());

        Officer officer = new Officer(userDetails, officerSchedule, officerZones);
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
}
