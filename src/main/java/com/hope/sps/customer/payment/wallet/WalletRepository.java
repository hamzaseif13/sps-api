package com.hope.sps.customer.payment.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Modifying
    @Query("UPDATE Wallet w SET w.balance=w.balance+:amount WHERE w.id =:id")
    void updateBalance(@Param("id") Long walletId, @Param("amount") double amount);
}
