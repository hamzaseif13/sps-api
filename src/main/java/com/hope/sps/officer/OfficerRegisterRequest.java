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

    private String phone;

    public OfficerRegisterRequest(String firstName, String lastName, String email, String password, Time startsAt, Time endsAt, List<String> daysOfWeek, List<Long> zoneIds, String phone) {
        super(firstName, lastName, email, password);
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.daysOfWeek = daysOfWeek;
        this.zoneIds = zoneIds;
        this.phone = phone;
    }
}
