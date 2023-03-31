package com.hope.sps.zone.space;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Query("SELECT COUNT(*) FROM Space s WHERE s.zone.id =: zoneId")
    Integer countSpaceByZoneId(Long zoneId);

    Integer countByStateIsAndId(Space.State state, Long id);

    void removeAllByZoneId(Long zoneId);
}
