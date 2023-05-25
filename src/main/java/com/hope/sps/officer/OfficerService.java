package com.hope.sps.officer;

import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.user_information.UserRepository;
import com.hope.sps.util.Validator;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficerService {

    private final OfficerRepository officerRepository;

    private final ZoneRepository zoneRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public List<OfficerDTO> getAll() {
        final var officerDTOs = new ArrayList<OfficerDTO>();

        officerRepository.findAll().forEach(officer ->
                officerDTOs.add(
                        mapper.map(officer, OfficerDTO.class)
                ));
        return officerDTOs;
    }

    public Long registerOfficer(final OfficerRegisterRequest request) {

        final var userInformation = mapper.map(request, UserInformation.class);
        if (userRepository.existsByEmail(userInformation.getEmail()))
            throw new DuplicateResourceException("email already exists");

        if (!request.getPassword().matches(Validator.passwordValidationRegex))
            throw new InvalidResourceProvidedException("invalid password");

        if (request.getStartsAt().after(request.getEndsAt())) {
            throw new InvalidResourceProvidedException("Start time cant be before end time");
        }

        userInformation.setRole(Role.OFFICER);
        userInformation.setPassword(passwordEncoder.encode(userInformation.getPassword()));

        final var officerSchedule = Schedule.builder()
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

        final var officer = new Officer(
                userInformation,
                officerSchedule,
                request.getPhoneNumber(),
                new HashSet<>(zonesByIds)
        );

        return officerRepository.save(officer).getId();
    }

    public void updateOfficer(final OfficerUpdateRequest request, final Long officerId) {

        if (request.getStartsAt().after(request.getEndsAt())) {
            throw new InvalidResourceProvidedException("Start time cant be before end time");
        }

        final var oldOfficer = officerRepository.findById(officerId).
                orElseThrow(() ->
                        new ResourceNotFoundException("officer not found")
                );

        // updating schedule info
        final var schedule = oldOfficer.getSchedule();
        schedule.setNewData(request);

        // assigning zones to officer
        final var zones = new HashSet<>(zoneRepository.findAllById(request.getZoneIds()));
        oldOfficer.setZones(zones);

        officerRepository.save(oldOfficer);
    }


    public OfficerDTO getOfficerById(final Long officerId) {

        final var officer = officerRepository.findById(officerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("could not found officer with id: {%s}".formatted(officerId))
                );

        return mapper.map(officer, OfficerDTO.class);
    }

    public void deleteOfficerById(final Long officerId) {
        if (!officerRepository.existsById(officerId)) {
            throw new ResourceNotFoundException("could not delete officer with id: {%s}, no officer found".formatted(officerId));
        }

        officerRepository.deleteById(officerId);
    }
}
