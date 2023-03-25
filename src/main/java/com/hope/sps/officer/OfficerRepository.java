package com.hope.sps.officer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfficerRepository extends JpaRepository<Officer, Long> {

    @Query("SELECT o.id FROM Officer o WHERE o.userDetails.id =:uDetailsId")
    Long getOfficerIdByUserDetailsId(@Param("uDetailsId") Long userDetailsId);
}
