package com.hope.sps.zone.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Modifying
    @Query("UPDATE Space s SET s.state=:state WHERE s.id=:id")
    void updateSpaceState(@Param("id") Long spaceId, @Param("state") Space.State state);

    boolean existsByIdAndStateIs(@Param("id") Long spaceId, @Param("state") Space.State state);

    @Query(value = "delete from space where zone_id =:id ", nativeQuery = true)
    @Modifying
    void removeAllByZoneId(@Param("id") Long zoneId);
}
