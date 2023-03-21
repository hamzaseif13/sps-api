package com.hope.sps.zone.space;

import com.hope.sps.UserDetails.UserDetailsImpl;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name ="space")
@EqualsAndHashCode
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    @Enumerated(EnumType.STRING)
    private State state;

    public enum State {
        AVAILABLE,TAKEN
    }

}
