package com.hope.sps.officer;

import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Time endsAt;
    Time startsAt;

    @ElementCollection(targetClass = DayOfWeek.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_days_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="schedule_id")
    Set<Zone> zones;
}
