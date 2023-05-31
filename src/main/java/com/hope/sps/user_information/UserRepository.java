package com.hope.sps.user_information;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInformation, Long> {
    Optional<UserInformation> findByEmail(String email);

    boolean existsByEmail(String email);
}
