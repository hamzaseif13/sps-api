package com.hope.sps.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateZoneRequest {
    Double fee;
    String title;
    Integer numberOfSpaces;
}
