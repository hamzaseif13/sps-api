package com.hope.sps.zone;

public record ZoneUpdateRequest(
        Double fee,
        String title,
        Integer numberOfSpaces,
        String startsAt,
        String endsAt
) {
}
