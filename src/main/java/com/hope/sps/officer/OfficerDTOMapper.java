package com.hope.sps.officer;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OfficerDTOMapper implements Function<Officer, OfficerDTO> {

    @Override
    public OfficerDTO apply(Officer officer) {
        var userDetails = officer.getUserDetails();

        return new OfficerDTO(
                officer.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                officer.getSchedule(),
                officer.getZones(),
                officer.getPhone()
        );
    }
}
