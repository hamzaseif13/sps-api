package com.hope.sps.zone;

public record ZoneRegistrationRequest(
        String tag,
        String title,
        Double fee,
        String address,
        Double lng,
        Double ltd,
        Integer numberOfSpaces,
        String startsAt,
        String endsAt
) {


}
