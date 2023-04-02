package com.hope.sps.zone;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.function.Function;

@Component
public class ZoneRegistrationRequestMapper implements Function<ZoneRegistrationRequest, Zone> {

    @Override
    public Zone apply(ZoneRegistrationRequest request) {

        return Zone.builder()
                .title(request.title())
                .tag(request.tag())
                .fee(request.fee())
                .location(new Zone.Location(request.address(), request.lng(), request.lat()))
                .numberOfSpaces(request.numberOfSpaces())
                .startsAt(Time.valueOf(request.startsAt()))
                .endsAt(Time.valueOf(request.endsAt()))
                .build();
    }
}
