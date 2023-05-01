package com.hope.sps.officer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.zone.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "phoneNumber", "schedule", "zones"})
public class OfficerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Schedule schedule;

    private Set<Zone> zones;

}
