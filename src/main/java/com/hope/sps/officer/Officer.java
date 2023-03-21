package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;
    @OneToOne
    UserDetailsImpl userDetails;

    @OneToOne
    Schedule schedule;



}
