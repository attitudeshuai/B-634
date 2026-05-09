package com.campus.repository;

import com.campus.entity.Registration;
import com.campus.entity.Registration.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserId(Long userId);
    List<Registration> findByActivityId(Long activityId);
    Optional<Registration> findByUserIdAndActivityIdAndStatus(Long userId, Long activityId, RegistrationStatus status);
    long countByActivityIdAndStatus(Long activityId, RegistrationStatus status);
}
