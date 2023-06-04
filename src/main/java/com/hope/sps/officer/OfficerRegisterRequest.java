package com.hope.sps.officer;

import com.hope.sps.common.RegisterRequest;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OfficerRegisterRequest extends RegisterRequest {

    private Time startsAt;

    private Time endsAt;

    @Size(min = 1, message = "officer must be assigned at least one day")
    private List<String> daysOfWeek;

    private List<Long> zoneIds;

    @Length(min = 10, max = 20, message = "invalid phoneNumber")
    private String phoneNumber;

    public OfficerRegisterRequest(String firstName, String lastName, String email, String password, Time startsAt, Time endsAt, List<String> daysOfWeek, List<Long> zoneIds, String phoneNumber) {
        super(firstName, lastName, email, password);
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.daysOfWeek = daysOfWeek;
        this.zoneIds = zoneIds;
        this.phoneNumber = phoneNumber;
    }
}
