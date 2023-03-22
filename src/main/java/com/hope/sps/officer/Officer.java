package com.hope.sps.officer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@AllArgsConstructor@NoArgsConstructor@ToString
@Data
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;
    @OneToOne(cascade = CascadeType.PERSIST)
    UserDetailsImpl userDetails;

    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    Schedule schedule;



}
