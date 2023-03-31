package com.hope.sps.zone.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SpaceRepository extends JpaRepository<Space, Long> {


    @Query(value = "delete from space where zone_id = ?1 ",nativeQuery = true)
    @Transactional
    @Modifying
    void removeAllByZoneId(Long zoneId);
}
