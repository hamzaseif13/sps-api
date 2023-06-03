package com.hope.sps.violation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.officer.OfficerDTO;
import com.hope.sps.zone.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonPropertyOrder({"id", "plateNumber", "carBrand", "carColor", "details", "imageUrl", "createdAt", "officer", "zone"})
public class ViolationDTO {

    private Long id;

    private String plateNumber;

    private String carBrand;

    private String carColor;

    private String details;

    private String imageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private OfficerDTO officer;

    private Zone zone;
}
