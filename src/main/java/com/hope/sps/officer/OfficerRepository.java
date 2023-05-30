package com.hope.sps.officer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OfficerRepository extends JpaRepository<Officer, Long> {

    Optional<Officer> getOfficerByUserInformationEmail(@Param("email") final String officerEmail);
}
