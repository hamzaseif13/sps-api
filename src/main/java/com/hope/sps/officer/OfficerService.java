package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.OfficerRegisterRequest;
import com.hope.sps.dto.RegisterRequestMapper;
import com.hope.sps.dto.ScheduleMapper;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.util.RegistrationUtil;
import com.hope.sps.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
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

        var officer = new Officer(userDetails, officerSchedule, request.getPhone(),officerZones);

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
        Time startsAt =Time.valueOf(request.getStartsAt());
        Time endsAt =Time.valueOf(request.getEndsAt());

        if(startsAt.after(endsAt)){
            throw new IllegalArgumentException("Start time cant be bfore end time");
        }
        if(request.getDaysOfWeeks().size()<1){
            throw new IllegalArgumentException("at least one day bro wtf");
        }
        Officer oldOfficer = officerRepository.findById(officerId).orElseThrow(()->new UsernameNotFoundException("officer not found"));
        var schedule =Schedule.
                builder().
                daysOfWeek(request.getDaysOfWeeks().stream().map(d-> DayOfWeek.valueOf(d)).collect(Collectors.toSet())).
                startsAt(startsAt).
                endsAt(endsAt).
                build();
        // todo update zonesIDS when abed finishes
        oldOfficer.setSchedule(schedule);
        officerRepository.save(oldOfficer);

    }

    private Set<Zone> getOfficeZonesFromReq(OfficerRegisterRequest request) {
        return request
                .getZoneIds()
                .stream()
                .map(Zone::new)
                .collect(Collectors.toSet());
    }

    public OfficerDTO getOfficerById(Long id){
        Officer officer = officerRepository.findById(id).orElseThrow(()->new UsernameNotFoundException(""));
        return officerDTOMapper.apply(officer);
    }

    public void deleteOfficerById(Long id) {
        officerRepository.deleteById(id);
    }
}
