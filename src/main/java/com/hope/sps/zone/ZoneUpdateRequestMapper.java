package com.hope.sps.zone;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.function.Function;

@Component
public class ZoneUpdateRequestMapper implements Function<ZoneUpdateRequest, Zone> {

    @Override
    public Zone apply(ZoneUpdateRequest zoneUpdateRequest) {
        return Zone.builder()
                .fee(zoneUpdateRequest.fee())
                .title(zoneUpdateRequest.title())
                .numberOfSpaces(zoneUpdateRequest.numberOfSpaces())
                .startsAt(Time.valueOf(zoneUpdateRequest.startsAt()))
                .endsAt(Time.valueOf(zoneUpdateRequest.endsAt()))
                .build();
    }
}
