package com.hope.sps.officer;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficerUpdateRequest {

    private Time startsAt;

    private Time endsAt;

    @Size(min = 1, message = "officer must work at least one day weekly")
    private List<String> daysOfWeek;

    @Size(min = 1, message = "officer must be responsible for at least one zone")
    private List<Long> zoneIds;

}
