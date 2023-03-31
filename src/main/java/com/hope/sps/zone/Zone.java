package com.hope.sps.zone;

import com.hope.sps.model.BaseEntity;
import com.hope.sps.zone.space.Space;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "zone")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class Zone extends BaseEntity {

    @Column(name = "tag", nullable = false, length = 20)
    private String tag;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "fee", nullable = false)
    @Min(0)
    private Double fee;

    @Column(name = "number_of_spaces", nullable = false)
    @Min(1)
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

    public Zone(Long Id) {
        setId(Id);
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private String address;
        private Double lng;
        private Double ltd;
    }
}
