package com.hope.sps.zone;

public record ZoneRegistrationRequest(
        String tag,
        String title,
        Double fee,
        String address,
        Double lng,
        Double lat,
        Integer numberOfSpaces,
        String startsAt,
        String endsAt
) {


}
