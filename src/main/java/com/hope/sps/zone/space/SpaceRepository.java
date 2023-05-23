package com.hope.sps.zone.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    boolean existsByZoneIdAndNumberAndStateIs(@Param("zoneId") Long zoneId, @Param("number") Integer spaceNumber, @Param("state") Space.State state);

    Optional<Space> findByZoneIdAndNumber(@Param("zoneId") Long zoneId, @Param("spaceNum") Integer spaceNumber);

    @Query(value = "delete from space where zone_id =:id ", nativeQuery = true)
    @Modifying
    void removeAllByZoneId(@Param("id") Long zoneId);
}
