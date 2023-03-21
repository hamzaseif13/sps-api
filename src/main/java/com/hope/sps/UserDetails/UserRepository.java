package com.hope.sps.UserDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetailsImpl,Long> {
    Optional<UserDetailsImpl> findByEmail(String email);
}
