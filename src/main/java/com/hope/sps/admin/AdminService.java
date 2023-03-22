package com.hope.sps.admin;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.AssignScheduleRequest;
import com.hope.sps.dto.NewZoneRequest;
import com.hope.sps.dto.OfficerRegisterRequest;
import com.hope.sps.dto.UpdateZoneRequest;
import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerRepository;
import com.hope.sps.officer.Schedule;
import com.hope.sps.officer.ScheduleRepository;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final OfficerRepository officerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ZoneRepository zoneRepository;
    private final ScheduleRepository scheduleRepository;

    public void registerOfficer(OfficerRegisterRequest request) {
        UserDetailsImpl userDetails = UserDetailsImpl
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.OFFICER)
                .build();
        Schedule schedule = Schedule
                .builder()
                .daysOfWeek(request.getDaysOfWeeks().stream().map(DayOfWeek::valueOf).collect(Collectors.toSet()))
                .startsAt(Time.valueOf(request.getStartsAt()))
                .endsAt(Time.valueOf(request.getEndsAt()))
                .build();
        Officer officer = Officer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userDetails(userDetails)
                .schedule(schedule)
                .build();
        officerRepository.save(officer);
    }

    public void assignSchedule(AssignScheduleRequest request, Long id) {
//        Officer officer = officerRepository.findById(id).orElseThrow(
//                ()-> new UsernameNotFoundException("Officer with this id does not exist")
//        );
//        scheduleRepository.delete(officer.getSchedule());
//        zoneRepository.findAllById(request.getZonedIds());
//        Schedule schedule = Schedule
//                .builder()
//                .daysOfWeek(request.getDaysOfWeeks().stream().map(DayOfWeek::valueOf).collect(Collectors.toSet()))
//                .startsAt(Time.valueOf(request.getStartsAt()))
//                .endsAt(Time.valueOf(request.getEndsAt()))
//                .zones(null)
//                .build();
//        officer.setSchedule(schedule);
//        officerRepository.save(officer);
    }

    public void addParkingZone(NewZoneRequest request) {
        Zone zone = Zone.builder()
                .fee(request.getFee())
                .title(request.getTitle())
                .location(new Zone.Location(request.getAddress(),
                        request.getLng(), request.getLtd()))
                .numberOfSpaces(request.getNumberOfSpaces())
                .build();
        // generate n number of spaces
        Set<Space> spaces = IntStream.rangeClosed(1,request.getNumberOfSpaces()).mapToObj(i -> new Space(i))
                .collect(Collectors.toSet());
        zone.setSpaces(spaces);
        zoneRepository.save(zone);
    }

    public void updateZone(UpdateZoneRequest request, Long id) {
        boolean flag = false;
        Zone zone = zoneRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("no zone idiot"));
        if(request.getNumberOfSpaces()!=null){
            zone.setNumberOfSpaces(request.getNumberOfSpaces());
            flag = true;
        }
        if(request.getFee()!=null){
            zone.setFee(request.getFee());
            flag = true;
        }
        if(request.getTitle()!=null){
            zone.setTitle(request.getTitle());
            flag = true;
        }
        if(flag){
            zoneRepository.save(zone);
        }
        throw new IllegalArgumentException("no feilds updated wtf ??");
    }
}
