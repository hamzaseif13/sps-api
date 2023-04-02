package com.hope.sps.zone;

import java.sql.Time;

public record ZoneUpdateRequest(
        Double fee,
        String title,
        Integer numberOfSpaces,
        Time startsAt,
        Time endsAt
) {
}
