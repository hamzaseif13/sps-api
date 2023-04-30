package com.hope.sps.zone.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    boolean existsByIdAndStateIs(Long id, Space.State state);


    @Query(value = "delete from space where zone_id =:id ", nativeQuery = true)
    @Transactional
    @Modifying
    void removeAllByZoneId(@Param("id") Long zoneId);
}
