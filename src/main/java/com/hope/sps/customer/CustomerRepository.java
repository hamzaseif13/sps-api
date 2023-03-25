package com.hope.sps.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c.id FROM Officer c WHERE c.userDetails.id =:uDetailsId")
    Long getCustomerIdByUserDetailsId(@Param("uDetailsId") Long userDetailsId);
}
