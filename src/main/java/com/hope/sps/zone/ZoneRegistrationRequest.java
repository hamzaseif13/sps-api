package com.hope.sps.zone;

import java.sql.Time;

public record ZoneRegistrationRequest(
        String tag,
        String title,
        Double fee,
        String address,
        Double lng,
        Double lat,
        Integer numberOfSpaces,
        Time startsAt,
        Time endsAt
) {


}
