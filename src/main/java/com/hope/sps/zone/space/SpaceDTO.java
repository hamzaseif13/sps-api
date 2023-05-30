package com.hope.sps.zone.space;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "number", "state"})
public class SpaceDTO {

    private Long id;

    private Integer number;

    private Space.State state;
}
