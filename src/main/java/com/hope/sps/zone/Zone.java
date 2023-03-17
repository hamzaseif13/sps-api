package com.hope.sps.zone;

import com.hope.sps.zone.space.Space;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name ="zone")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double fee;

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    private Set<Space> spaces;

    @Embedded
    private Location location;
    @Embeddable
    private static class Location{
        String address;
        Double lng;
        Double ltd;
    }
}
