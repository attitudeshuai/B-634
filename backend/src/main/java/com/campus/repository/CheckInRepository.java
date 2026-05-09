package com.campus.repository;

import com.campus.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByRegistrationId(Long registrationId);
    boolean existsByRegistrationId(Long registrationId);
}
