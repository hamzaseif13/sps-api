package com.hope.sps.zone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    boolean existsByTag(String tag);

    @Query("SELECT z.fee FROM Zone z WHERE z.id=:id")
    double getFeeById(@Param("id") Long zoneId);
}
