package com.hope.sps.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfficerRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> daysOfWeeks;
    private String startsAt;
    private String endsAt;
}
