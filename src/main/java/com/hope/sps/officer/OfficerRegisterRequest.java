package com.hope.sps.officer;

import com.hope.sps.dto.RegisterRequest;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class OfficerRegisterRequest extends RegisterRequest {

    private Time startsAt;

    private Time endsAt;

    private List<String> daysOfWeek;

    private List<Long> zoneIds;
    private Long phone;

}
