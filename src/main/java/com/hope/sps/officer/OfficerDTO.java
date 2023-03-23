package com.hope.sps.officer;

import com.hope.sps.zone.Zone;

import java.util.Set;

public record OfficerDTO(
        String firstName,
        String lastName,
        String email,
        Schedule schedule,
        Set<Zone> zones
) {

}
