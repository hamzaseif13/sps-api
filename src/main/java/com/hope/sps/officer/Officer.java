package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.model.BaseEntity;
import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "officer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Officer extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetailsImpl userDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Schedule schedule;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "officer_id")
    private Set<Zone> zones;

    public void addZone(final Zone zone) {
        if (zone == null)
            zones = new HashSet<>();
        this.zones.add(zone);
    }

}
