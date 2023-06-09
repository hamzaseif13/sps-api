package com.hope.sps.zone.space;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "space")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false, length = 50)
    private Integer number;

    @Enumerated(EnumType.STRING)
    private State state = State.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Zone zone;

    public Space(Integer number) {
        this.number = number;
    }

    // for testing purposes
    public Space(long id, int number, State state) {
        this.id = id;
        this.number = number;
        this.state = state;
    }

    //delete
    public boolean isAvailable() {
        return this.state == State.AVAILABLE;
    }

    public enum State {
        AVAILABLE, TAKEN
    }

}
