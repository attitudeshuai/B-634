package com.campus.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
    
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
    
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.ACTIVE;
    
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
    
    public enum RegistrationStatus {
        ACTIVE,     // 报名成功
        CANCELLED   // 已取消
    }
}
