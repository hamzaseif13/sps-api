package com.hope.sps.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("SELECT a.id FROM Admin a WHERE a.userDetails.id =:uDetailsId")
    Long getAdminIdByUserDetailsId(@Param("uDetailsId") Long userDetailsId);
}
