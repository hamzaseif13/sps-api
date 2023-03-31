package com.hope.sps.zone;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ZoneDTOMapper implements Function<Zone, ZoneDTO> {

    @Override
    public ZoneDTO apply(Zone zone) {
        return new ZoneDTO(
                zone.getId(),
                zone.getTag(),
                zone.getTitle(),
                zone.getFee(),
                zone.getLocation().getAddress(),
                zone.getLocation().getLng(),
                zone.getLocation().getLtd(),
                zone.getNumberOfSpaces(),
                String.valueOf(zone.getStartsAt()),
                String.valueOf(zone.getEndsAt()),
                0,
                0
        );

    }
}
