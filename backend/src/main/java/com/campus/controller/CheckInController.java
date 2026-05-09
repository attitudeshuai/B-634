package com.campus.controller;

import com.campus.entity.CheckIn;
import com.campus.service.CheckInService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckInController {
    
    private final CheckInService checkInService;
    
    @PostMapping
    public ResponseEntity<?> checkIn(@RequestBody CheckInRequest request) {
        try {
            CheckIn checkIn = checkInService.checkIn(request.getRegistrationId());
            return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/registration/{registrationId}")
    public ResponseEntity<CheckIn> getCheckInByRegistrationId(@PathVariable Long registrationId) {
        return checkInService.getCheckInByRegistrationId(registrationId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{registrationId}")
    public ResponseEntity<CheckInStatus> getCheckInStatus(@PathVariable Long registrationId) {
        boolean checkedIn = checkInService.isCheckedIn(registrationId);
        return ResponseEntity.ok(new CheckInStatus(checkedIn));
    }
    
    @Data
    public static class CheckInRequest {
        private Long registrationId;
    }
    
    @Data
    public static class CheckInStatus {
        private boolean checkedIn;
        
        public CheckInStatus(boolean checkedIn) {
            this.checkedIn = checkedIn;
        }
    }
    
    @Data
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
