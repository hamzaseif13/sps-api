package com.hope.sps.dto;

import com.hope.sps.officer.schedule.Schedule;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ScheduleMapper implements Function<OfficerRegisterRequest, Schedule> {

    @Override
    public Schedule apply(OfficerRegisterRequest request) {
        return Schedule.builder()
                .daysOfWeek(request.getDaysOfWeeks()
                        .stream()
                        .map(DayOfWeek::valueOf)
                        .collect(Collectors.toSet())
                )
                .startsAt(Time.valueOf(request.getStartsAt()))
                .endsAt(Time.valueOf(request.getEndsAt()))
                .build();
    }
}
