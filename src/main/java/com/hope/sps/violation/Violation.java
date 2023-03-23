package com.hope.sps.violation;


import com.hope.sps.customer.Customer;
import com.hope.sps.model.BaseEntity;
import com.hope.sps.officer.Officer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "violation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Violation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private Officer officer;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "image_url", nullable = false)
    private String imageUrl; //todo add s3 hahah
}
