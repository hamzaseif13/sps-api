package com.hope.sps.violation;


import com.hope.sps.customer.Customer;
import com.hope.sps.officer.Officer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "violation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Violation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private Officer officer;

    @Column(name = "plate_number", nullable = false)
    private String plateNumber;

    @Column(name = "car_brand", nullable = false)
    private String carBrand;

    @Column(name = "car_color", nullable = false)
    private String carColor;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
