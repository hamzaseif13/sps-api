package com.hope.sps.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class NewZoneRequest {
    private String title;
    private Double fee;
    private String address;
    private Double lng;
    private Double ltd;
    private Integer numberOfSpaces;
}
