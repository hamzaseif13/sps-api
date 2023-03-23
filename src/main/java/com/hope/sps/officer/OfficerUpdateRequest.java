package com.hope.sps.officer;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OfficerUpdateRequest {

    private String startsAt;

    private String endsAt;

    private List<String> daysOfWeeks;

    private List<Long> zoneIds;

}
