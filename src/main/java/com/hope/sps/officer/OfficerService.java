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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.DayOfWeek;
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

    @Transactional(readOnly = true)
    public List<OfficerDTO> getAll() {
        return officerRepository.findAll()
                .stream()
                .map(officer -> mapper.map(officer, OfficerDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public OfficerDTO getOfficerById(final Long officerId) {

        final var officer = getOfficerByIdFromDB(officerId);

        return mapper.map(officer, OfficerDTO.class);
    }

    @Transactional
    public Long registerOfficer(final OfficerRegisterRequest request) {

        // get userInformation object from the request
        final var userInformation = mapper.map(request, UserInformation.class);

        // email already exists? throw exception
        throwExceptionIfEmailAlreadyExists(userInformation.getEmail());

        // password invalid? throw exception
        Validator.validateUserPassword(request.getPassword());

        // end time before start time? throw exception
        throwExceptionIfInvalidScheduleTime(request.getStartsAt(), request.getEndsAt());

        // assign OFFICER role and hash officer's password
        userInformation.setRole(Role.OFFICER);
        userInformation.setPassword(passwordEncoder.encode(userInformation.getPassword()));

        // generate officerSchedule
        final var officerSchedule = assempleOfficerSchedule(request);

        // get all zones that officer responsible for
        final List<Zone> zonesByIds = zoneRepository.findAllById(request.getZoneIds());

        // assemble an officer object, save it and return its generated id
        final var officer = new Officer(
                userInformation,
                officerSchedule,
                request.getPhoneNumber(),
                new HashSet<>(zonesByIds)
        );

        return officerRepository.save(officer).getId();
    }

    @Transactional
    public void updateOfficer(final OfficerUpdateRequest request, final Long officerId) {

        // end time before start time? throw exception
        throwExceptionIfInvalidScheduleTime(request.getStartsAt(), request.getEndsAt());

        // get to update officer information
        final var toUpdateOfficer = getOfficerByIdFromDB(officerId);

        // updating toUpdateOfficer schedule's info
        final var schedule = toUpdateOfficer.getSchedule();
        schedule.setNewData(request);

        // updating toUpdateOfficer zones
        final var zones = new HashSet<>(zoneRepository.findAllById(request.getZoneIds()));
        toUpdateOfficer.setZones(zones);

        officerRepository.save(toUpdateOfficer);
    }

    public void deleteOfficerById(final Long officerId) {
        if (!officerRepository.existsById(officerId)) {
            throw new ResourceNotFoundException(
                    "could not delete officer with id: {%s}, no officer found".formatted(officerId)
            );

        }
        officerRepository.deleteById(officerId);
    }


    /************ HELPER_METHOD **************/
    private void throwExceptionIfEmailAlreadyExists(final String officerEmail) {
        if (userRepository.existsByEmail(officerEmail))
            throw new DuplicateResourceException("email already exists");
    }

    private void throwExceptionIfInvalidScheduleTime(final Time startsAt, final Time endsAt) {
        if (startsAt.after(endsAt)) {
            throw new InvalidResourceProvidedException("Start time cant be before end time");
        }
    }

    private Officer getOfficerByIdFromDB(final Long officerId) {
        return officerRepository.findById(officerId).
                orElseThrow(() ->
                        new ResourceNotFoundException("could not found officer with id: {%s}".formatted(officerId))
                );
    }

    private Schedule assempleOfficerSchedule(final OfficerRegisterRequest request) {
        return Schedule.builder()
                .daysOfWeek(
                        request.getDaysOfWeek()
                                .stream()
                                .map(DayOfWeek::valueOf)
                                .collect(Collectors.toSet())
                )
                .startsAt(request.getStartsAt())
                .endsAt(request.getEndsAt())
                .build();
    }
}
