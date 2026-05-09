package com.campus.service;

import com.campus.entity.CheckIn;
import com.campus.entity.Registration;
import com.campus.repository.CheckInRepository;
import com.campus.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckInService {
    
    private final CheckInRepository checkInRepository;
    private final RegistrationRepository registrationRepository;
    
    @Transactional
    public CheckIn checkIn(Long registrationId) {
        log.info("Processing check-in for registration id: {}", registrationId);
        
        // Check if already checked in
        if (checkInRepository.existsByRegistrationId(registrationId)) {
            throw new RuntimeException("Already checked in");
        }
        
        // Get registration
        Registration registration = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration not found with id: " + registrationId));
        
        // Verify registration is active
        if (registration.getStatus() != Registration.RegistrationStatus.ACTIVE) {
            throw new RuntimeException("Registration is not active");
        }
        
        // Create check-in
        CheckIn checkIn = new CheckIn();
        checkIn.setRegistration(registration);
        
        CheckIn saved = checkInRepository.save(checkIn);
        log.info("Check-in successful for registration id: {}", registrationId);
        return saved;
    }
    
    public Optional<CheckIn> getCheckInByRegistrationId(Long registrationId) {
        log.info("Fetching check-in for registration id: {}", registrationId);
        return checkInRepository.findByRegistrationId(registrationId);
    }
    
    public boolean isCheckedIn(Long registrationId) {
        return checkInRepository.existsByRegistrationId(registrationId);
    }
}
