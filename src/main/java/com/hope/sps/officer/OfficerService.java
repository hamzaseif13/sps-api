package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.RegisterRequestMapper;
import com.hope.sps.exception.InvalidResourceException;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficerService {

    private final RegisterRequestMapper registerRequestMapper;

    private final OfficerDTOMapper officerDTOMapper;

    private final OfficerRepository officerRepository;

    private final ZoneRepository zoneRepository;

    public Long registerOfficer(OfficerRegisterRequest request) {

        UserDetailsImpl userDetails = registerRequestMapper.apply(request);

        // todo add password validator for customer officer and admin

        Schedule officerSchedule = Schedule.builder().
                daysOfWeek(request.getDaysOfWeek().stream().map(DayOfWeek::valueOf).collect(Collectors.toSet())).
                startsAt(request.getStartsAt()).
                endsAt(request.getEndsAt()).
                build();

        Set<Zone> zones = new HashSet<>(zoneRepository.findAllById(request.getZoneIds()));
        Officer officer = new Officer(userDetails, officerSchedule, request.getPhone(),zones);


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

        if(request.getStartsAt().after(request.getEndsAt())){
            throw new InvalidResourceException("Start time cant be before end time");
        }
        if(request.getDaysOfWeek().size()<1){
            throw new InvalidResourceException("officer should have at least one day");
        }
        Officer oldOfficer = officerRepository.findById(officerId).
                orElseThrow(()->new UsernameNotFoundException("officer not found"));
        // updating schedule info
        Schedule schedule = oldOfficer.getSchedule();
        schedule.setNewData(request);
        // assigning zones to officer
        Set<Zone> zones = new HashSet<>(zoneRepository.findAllById(request.getZoneIds()));
        oldOfficer.setZones(zones);
        officerRepository.save(oldOfficer);

    }



    public OfficerDTO getOfficerById(Long id){
        Officer officer = officerRepository.findById(id).orElseThrow(()->new UsernameNotFoundException(""));
        return officerDTOMapper.apply(officer);
    }

    public void deleteOfficerById(Long id) {
        officerRepository.deleteById(id);
    }
}
