package com.hope.sps.zone;

import com.hope.sps.zone.space.Space;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "zone")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", nullable = false, length = 20, unique = true)
    private String tag;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "fee", nullable = false)
    private Double fee;

    @Column(name = "number_of_spaces", nullable = false)
    private Integer numberOfSpaces;

    @Column(name = "start_at", nullable = false)
    private Time startsAt;

    @Column(name = "ends_at", nullable = false)
    private Time endsAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    private Set<Space> spaces;

    @Embedded
    private Location location;

    public void setAddress(String address) {
        if (this.location == null)
            location = new Location();

        location.setAddress(address);
    }

    public void setLng(Double lng) {
        if (this.location == null)
            location = new Location();

        location.setLng(lng);
    }

    public void setLat(Double lat) {
        if (this.location == null)
            location = new Location();

        location.setLat(lat);
    }


    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {

        private String address;

        private Double lng;

        private Double lat;

    }
}
