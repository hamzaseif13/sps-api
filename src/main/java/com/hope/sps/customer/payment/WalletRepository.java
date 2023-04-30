package com.hope.sps.customer.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("UPDATE Wallet w SET w.balance=:balance WHERE w.id=:id")
    Long updateBalance(@Param("id") Long walletId, @Param("balance") double updatedBalance);
}
