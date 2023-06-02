package com.hope.sps.officer.schedule;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.zone.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"startsAt", "endsAt", "zones","daysOfWeek"})
public class ScheduleDTO {

    private Time startsAt;

    private Time endsAt;

    private Set<Zone> zones;
    private Set<DayOfWeek> daysOfWeek;
}
