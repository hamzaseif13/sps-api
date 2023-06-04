package com.hope.sps.violation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long> {

    List<Violation> findByOfficerUserInformationEmail(@Param("email") final String email);

    List<Violation> findByOfficerId(@Param("OfficerId") Long officerId);
}
