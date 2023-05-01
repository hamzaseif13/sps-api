package com.hope.sps.zone.space;

import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "space")
@Data
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

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Zone zone;

    public Space(Integer number) {
        this.number = number;
    }

    public Space(Long id) {
        this.id = id;
    }

    public enum State {
        AVAILABLE, TAKEN
    }

}
