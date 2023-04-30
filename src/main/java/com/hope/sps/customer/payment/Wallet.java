package com.hope.sps.customer.payment;

import com.hope.sps.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Wallet extends BaseEntity {

    @Column(name = "balance", nullable = false, length = 50)
    @Min(0)
    private Double balance;
}
