package com.hope.sps.officer;

import com.hope.sps.officer.schedule.Schedule;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Table(name = "officer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserInformation userInformation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id")
    private Set<Zone> zones;

    public Officer(UserInformation userInformation, Schedule schedule, String phoneNumber, Set<Zone> zones) {
        this.userInformation = userInformation;
        this.schedule = schedule;
        this.phoneNumber = phoneNumber;
        this.zones = zones;
    }
}
