package com.campus.controller;

import com.campus.entity.Registration;
import com.campus.service.RegistrationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    @GetMapping
    public ResponseEntity<List<Registration>> getAllRegistrations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long activityId) {
        if (userId != null) {
            return ResponseEntity.ok(registrationService.getRegistrationsByUserId(userId));
        }
        if (activityId != null) {
            return ResponseEntity.ok(registrationService.getRegistrationsByActivityId(activityId));
        }
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
        return registrationService.getRegistrationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createRegistration(@RequestBody RegistrationRequest request) {
        try {
            Registration created = registrationService.createRegistration(
                request.getUserId(), request.getActivityId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long id) {
        try {
            registrationService.cancelRegistration(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @Data
    public static class RegistrationRequest {
        private Long userId;
        private Long activityId;
    }
    
    @Data
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
