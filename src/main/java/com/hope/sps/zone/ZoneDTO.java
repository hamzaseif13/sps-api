package com.hope.sps.zone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    private Double ltd;

    private Integer numberOfSpaces;

    private String startsAt;

    private String endsAt;

    private Integer totalSpaces;

    private Integer availableSpaces;
}