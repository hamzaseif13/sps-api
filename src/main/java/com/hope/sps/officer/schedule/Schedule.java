package com.hope.sps.officer.schedule;

import com.hope.sps.model.BaseEntity;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Schedule extends BaseEntity {

    @Column(name = "start_at", nullable = false)
    private Time startsAt;

    @Column(name = "ends_at", nullable = false)
    private Time endsAt;

    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_days_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();
}
