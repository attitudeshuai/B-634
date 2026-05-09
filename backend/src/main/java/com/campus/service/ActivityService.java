package com.campus.service;

import com.campus.entity.Activity;
import com.campus.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {
    
    private final ActivityRepository activityRepository;
    
    public List<Activity> getAllActivities() {
        log.info("Fetching all activities");
        return activityRepository.findAllByOrderByStartTimeDesc();
    }
    
    public Optional<Activity> getActivityById(Long id) {
        log.info("Fetching activity with id: {}", id);
        return activityRepository.findById(id);
    }
    
    public List<Activity> getActivitiesByStatus(Activity.ActivityStatus status) {
        log.info("Fetching activities with status: {}", status);
        return activityRepository.findByStatusOrderByStartTimeDesc(status);
    }
    
    @Transactional
    public Activity createActivity(Activity activity) {
        log.info("Creating new activity: {}", activity.getTitle());
        
        if (activity.getMaxParticipants() == null || activity.getMaxParticipants() < 1) {
            throw new RuntimeException("最大参与人数必须大于0");
        }
        
        if (activity.getCurrentParticipants() == null) {
            activity.setCurrentParticipants(0);
        }
        
        return activityRepository.save(activity);
    }
    
    @Transactional
    public Activity updateActivity(Long id, Activity activityDetails) {
        log.info("Updating activity with id: {}", id);
        Activity activity = activityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        
        if (activityDetails.getMaxParticipants() != null && activityDetails.getMaxParticipants() < 1) {
            throw new RuntimeException("最大参与人数必须大于0");
        }
        
        if (activityDetails.getMaxParticipants() != null && 
            activityDetails.getMaxParticipants() < activity.getCurrentParticipants()) {
            throw new RuntimeException("最大参与人数不能小于当前已报名人数");
        }
        
        activity.setTitle(activityDetails.getTitle());
        activity.setDescription(activityDetails.getDescription());
        activity.setLocation(activityDetails.getLocation());
        activity.setStartTime(activityDetails.getStartTime());
        activity.setEndTime(activityDetails.getEndTime());
        if (activityDetails.getMaxParticipants() != null) {
            activity.setMaxParticipants(activityDetails.getMaxParticipants());
        }
        activity.setStatus(activityDetails.getStatus());
        
        return activityRepository.save(activity);
    }
    
    @Transactional
    public void deleteActivity(Long id) {
        log.info("Deleting activity with id: {}", id);
        activityRepository.deleteById(id);
    }
}
