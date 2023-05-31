package com.hope.sps.zone.space;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"customerName", "carBrand", "carColor", "spaceCreatedAt", "sessionDuration", "sessionExtended"})
public class OccupiedSpaceDTO {

    private String customerName;

    private String carBrand;

    private String carColor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime spaceCreatedAt;

    private Long sessionDuration;

    private boolean sessionExtended;
}
