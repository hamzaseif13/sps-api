package com.hope.sps.zone;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.zone.space.SpaceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "tag", "title", "fee", "address", "lng", "lat", "numberOfSpaces", "startsAt", "endsAt", "availableSpaces","spaceList"})
public class ZoneDTO {

    private Long id;

    private String tag;

    private String title;

    private Double fee;

    private String address;

    private Double lng;

    private Double lat;

    private Integer numberOfSpaces;

    private Time startsAt;

    private Time endsAt;

    private Long availableSpaces;

    private List<SpaceDTO> spaceList;
}
