package com.campus.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "check_ins")
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;
    
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;
    
    @PrePersist
    protected void onCreate() {
        checkInTime = LocalDateTime.now();
    }
}
