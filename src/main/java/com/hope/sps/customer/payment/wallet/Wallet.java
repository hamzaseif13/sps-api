package com.hope.sps.customer.payment.wallet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", nullable = false, length = 50)
    private BigDecimal balance = new BigDecimal("10.0");

    public Wallet(BigDecimal balance) {
        this.balance = balance;
    }
}
