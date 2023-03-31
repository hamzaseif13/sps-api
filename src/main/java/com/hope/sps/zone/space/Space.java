package com.hope.sps.zone.space;

import com.hope.sps.model.BaseEntity;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "space")
@EqualsAndHashCode(callSuper = true)
public class Space extends BaseEntity {

    @Column(name = "number", nullable = false, length = 50)
    @Min(1)
    private Integer number;

    @Enumerated(EnumType.STRING)
    private State state = State.AVAILABLE;

    public Space(Integer number) {
        this.number = number;
    }

    public enum State {
        AVAILABLE, TAKEN
    }

}
