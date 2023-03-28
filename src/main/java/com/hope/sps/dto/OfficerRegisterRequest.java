package com.hope.sps.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class OfficerRegisterRequest extends RegisterRequest {

    private String startsAt;

    private String endsAt;

    private List<String> daysOfWeek;

    private List<Long> zoneIds;
    private Long phone;

}
