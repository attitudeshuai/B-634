package com.campus.repository;

import com.campus.entity.Activity;
import com.campus.entity.Activity.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByStatusOrderByStartTimeDesc(ActivityStatus status);
    List<Activity> findAllByOrderByStartTimeDesc();
}
