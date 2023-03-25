package com.hope.sps.UserDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetailsImpl, Long> {
    Optional<UserDetailsImpl> findByEmail(String email);

    boolean existsByEmail(String email);
}
