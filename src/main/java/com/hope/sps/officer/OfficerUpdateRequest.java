package com.hope.sps.officer;


import lombok.*;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OfficerUpdateRequest {

    private Time startsAt;

    private Time endsAt;

    private List<String> daysOfWeek;

    private List<Long> zoneIds;

}
