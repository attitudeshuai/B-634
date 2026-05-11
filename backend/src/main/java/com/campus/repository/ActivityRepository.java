package com.campus.repository;

import com.campus.entity.Activity;
import com.campus.entity.Activity.ActivityStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByStatusOrderByStartTimeDesc(ActivityStatus status);
    List<Activity> findAllByOrderByStartTimeDesc();
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Activity a WHERE a.id = :id")
    Optional<Activity> findByIdWithLock(@Param("id") Long id);
}
