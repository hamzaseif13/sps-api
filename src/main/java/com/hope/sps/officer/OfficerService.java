package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.UserDetails.UserRepository;
import com.hope.sps.admin.EmployeeRegisterRequestMapper;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceException;
import com.hope.sps.exception.ResourceNotFoundException;
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

    private final EmployeeRegisterRequestMapper employeeRegisterRequestMapper;

    private final OfficerDTOMapper officerDTOMapper;

    private final OfficerRepository officerRepository;

    private final ZoneRepository zoneRepository;

    private final UserRepository userRepository;

    public List<OfficerDTO> getAll() {
        return officerRepository
                .findAll()
                .stream()
                .map(officerDTOMapper)
                .collect(Collectors.toList());
    }

    public Long registerOfficer(final OfficerRegisterRequest request) {

        final UserDetailsImpl userDetails = employeeRegisterRequestMapper.apply(request);

        if (userRepository.existsByEmail(userDetails.getEmail()))
            throw new DuplicateResourceException("email already exists");

        // todo add password validator for customer officer and admin <-WTF is this??????

        final Schedule officerSchedule = Schedule.builder()
                .daysOfWeek(
                        request.getDaysOfWeek()
                                .stream()
                                .map(DayOfWeek::valueOf)
                                .collect(Collectors.toSet())
                ).
                startsAt(request.getStartsAt()).
                endsAt(request.getEndsAt()).
                build();

        final List<Zone> zonesByIds = zoneRepository.findAllById(request.getZoneIds());

        final Officer officer = new Officer(userDetails, officerSchedule, request.getPhone(), new HashSet<>(zonesByIds));

        return officerRepository.save(officer).getId();
    }


    public void updateOfficer(final OfficerUpdateRequest request, final Long officerId) {

        if (request.getStartsAt().after(request.getEndsAt())) {
            throw new InvalidResourceException("Start time cant be before end time");
        }

        if (request.getDaysOfWeek().size() < 1) {
            throw new InvalidResourceException("officer should have at least one day");
        }

        Officer oldOfficer = officerRepository.findById(officerId).
                orElseThrow(() -> new ResourceNotFoundException("officer not found"));

        // updating schedule info
        Schedule schedule = oldOfficer.getSchedule();
        schedule.setNewData(request);

        // assigning zones to officer
        Set<Zone> zones = new HashSet<>(zoneRepository.findAllById(request.getZoneIds()));
        oldOfficer.setZones(zones);

        officerRepository.save(oldOfficer);
    }


    public OfficerDTO getOfficerById(final Long officerId) {

        final Officer officer = officerRepository.findById(officerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("could not found officer with id: {%s}".formatted(officerId))
                );

        return officerDTOMapper.apply(officer);
    }

    public void deleteOfficerById(final Long officerId) {
        if (!officerRepository.existsById(officerId)) {
            throw new ResourceNotFoundException("could not delete officer with id: {%s}, no officer found".formatted(officerId));
        }

        officerRepository.deleteById(officerId);
    }
}
