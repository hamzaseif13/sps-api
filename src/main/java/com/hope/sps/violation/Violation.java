package com.hope.sps.violation;


import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.customer.Customer;
import com.hope.sps.officer.Officer;
import com.hope.sps.zone.Zone;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Violation {
    @Id
    Integer id;

    String details;
    String imageUrl; //todo add s3 hahah

    @ManyToOne
    @JoinColumn(name="officer_id")
    Officer officer;

    @ManyToOne
    @JoinColumn(name="customer_id")
    Customer customer;


}
