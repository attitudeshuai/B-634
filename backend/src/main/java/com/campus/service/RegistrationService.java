package com.campus.service;

import com.campus.entity.Activity;
import com.campus.entity.Registration;
import com.campus.entity.Registration.RegistrationStatus;
import com.campus.entity.User;
import com.campus.repository.ActivityRepository;
import com.campus.repository.RegistrationRepository;
import com.campus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    
    public List<Registration> getAllRegistrations() {
        log.info("Fetching all registrations");
        return registrationRepository.findAll();
    }
    
    public List<Registration> getRegistrationsByUserId(Long userId) {
        log.info("Fetching registrations for user id: {}", userId);
        return registrationRepository.findByUserId(userId);
    }
    
    public List<Registration> getRegistrationsByActivityId(Long activityId) {
        log.info("Fetching registrations for activity id: {}", activityId);
        return registrationRepository.findByActivityId(activityId);
    }
    
    public Optional<Registration> getRegistrationById(Long id) {
        log.info("Fetching registration with id: {}", id);
        return registrationRepository.findById(id);
    }
    
    @Transactional
    public Registration createRegistration(Long userId, Long activityId) {
        log.info("Creating registration for user {} and activity {}", userId, activityId);
        
        // Check if already registered
        Optional<Registration> existingReg = registrationRepository
            .findByUserIdAndActivityIdAndStatus(userId, activityId, RegistrationStatus.ACTIVE);
        if (existingReg.isPresent()) {
            throw new RuntimeException("您已报名过该活动，请勿重复报名");
        }
        
        // Get user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户未找到: " + userId));
        
        // Get activity with pessimistic lock to prevent concurrent registration
        Activity activity = activityRepository.findByIdWithLock(activityId)
            .orElseThrow(() -> new RuntimeException("活动未找到: " + activityId));
        
        // Check if activity is full - using activity's currentParticipants (which is maintained consistently)
        Integer maxParticipants = activity.getMaxParticipants();
        if (maxParticipants != null && activity.getCurrentParticipants() >= maxParticipants) {
            throw new RuntimeException("该活动名额已满");
        }
        
        // Create registration
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setActivity(activity);
        registration.setStatus(RegistrationStatus.ACTIVE);
        
        Registration saved = registrationRepository.save(registration);
        
        // Update activity participant count
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activityRepository.save(activity);
        
        log.info("Registration created successfully with id: {}", saved.getId());
        return saved;
    }
    
    @Transactional
    public void cancelRegistration(Long id) {
        log.info("Cancelling registration with id: {}", id);
        Registration registration = registrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registration not found with id: " + id));
        
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
        
        // Update activity participant count with lock
        Activity activity = activityRepository.findByIdWithLock(registration.getActivity().getId())
            .orElseThrow(() -> new RuntimeException("Activity not found"));
        long currentCount = registrationRepository.countByActivityIdAndStatus(
            activity.getId(), RegistrationStatus.ACTIVE);
        activity.setCurrentParticipants((int) currentCount);
        activityRepository.save(activity);
        
        log.info("Registration cancelled successfully");
    }
}
