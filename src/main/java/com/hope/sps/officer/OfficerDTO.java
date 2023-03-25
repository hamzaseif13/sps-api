package com.hope.sps.officer;

import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.zone.Zone;

import java.util.Set;

public record OfficerDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Schedule schedule,
        Set<Zone> zones
) {

}
//id, first name, last name , phoneNumber , email