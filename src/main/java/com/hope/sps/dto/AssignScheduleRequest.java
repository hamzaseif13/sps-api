package com.hope.sps.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignScheduleRequest {

    private List<String> daysOfWeeks;

    private String startsAt;

    private String endsAt;

    private Set<Long> zonedIds;
}
