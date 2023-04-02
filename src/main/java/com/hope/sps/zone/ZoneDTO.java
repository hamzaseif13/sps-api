package com.hope.sps.zone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Time;

@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ZoneDTO {

    private Long zoneId;

    private String tag;

    private String title;

    private Double fee;

    private String address;

    private Double lng;

    private Double lat;

    private Integer numberOfSpaces;

    private Time startsAt;

    private Time endsAt;

    private Integer availableSpaces;
}
