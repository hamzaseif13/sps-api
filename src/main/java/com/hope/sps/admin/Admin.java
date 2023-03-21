package com.hope.sps.admin;

import com.hope.sps.UserDetails.UserDetailsImpl;
import jakarta.persistence.*;

@Entity
public class Admin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;
    @OneToOne
    UserDetailsImpl userDetails;

}
