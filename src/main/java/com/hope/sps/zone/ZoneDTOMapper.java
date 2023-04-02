package com.hope.sps.zone;

import com.hope.sps.zone.space.Space;
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
                zone.getLocation().getLat(),
                zone.getNumberOfSpaces(),
                String.valueOf(zone.getStartsAt()),
                String.valueOf(zone.getEndsAt()),
                getAvailableSpaces(zone)
        );

    }

    private Integer getAvailableSpaces(Zone zone) {
       return Math.toIntExact(zone.getSpaces().stream().filter(space -> space.getState() == Space.State.AVAILABLE).count());
    }
}
