package com.hope.sps.officer.schedule;

import com.hope.sps.officer.OfficerUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_at", nullable = false)
    private Time startsAt;

    @Column(name = "ends_at", nullable = false)
    private Time endsAt;

    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_days_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    public void setNewData(OfficerUpdateRequest request) {
        this.startsAt = request.getStartsAt();
        this.endsAt = request.getEndsAt();
        this.daysOfWeek = request.getDaysOfWeek().stream().map(DayOfWeek::valueOf).collect(Collectors.toSet());
    }
}
