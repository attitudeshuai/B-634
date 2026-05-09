package com.campus.config;

import com.campus.entity.Activity;
import com.campus.entity.Activity.ActivityStatus;
import com.campus.entity.User;
import com.campus.entity.Registration;
import com.campus.repository.ActivityRepository;
import com.campus.repository.UserRepository;
import com.campus.repository.RegistrationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {
    
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final RegistrationRepository registrationRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            log.info("Database already initialized, skipping seed data");
            return;
        }
        
        log.info("Initializing database with seed data...");
        
        // Create default accounts with encrypted passwords
        User admin = createUser("admin", "123456", "2020001", "admin@campus.edu", "13800138000", User.Role.ADMIN);
        User student1 = createUser("student1", "123456", "2021001", "student1@campus.edu", "13800138001", User.Role.STUDENT);
        User student2 = createUser("student2", "123456", "2021002", "student2@campus.edu", "13800138002", User.Role.STUDENT);
        
        // Create additional test users
        User user1 = createUser("张三", "123456", "2021003", "zhangsan@campus.edu", "13800138003", User.Role.STUDENT);
        User user2 = createUser("李四", "123456", "2021004", "lisi@campus.edu", "13800138004", User.Role.STUDENT);
        
        // Create activities
        Activity act1 = createActivity(
            "2024年春季运动会",
            "一年一度的春季运动会，包含田径、球类等多项比赛项目。欢迎全校师生积极参与！",
            "校体育场",
            LocalDateTime.now().plusDays(15),
            LocalDateTime.now().plusDays(15).plusHours(8),
            500,
            ActivityStatus.UPCOMING
        );
        
        Activity act2 = createActivity(
            "学术讲座：人工智能前沿技术",
            "邀请知名AI专家分享最新研究成果，探讨人工智能在各领域的应用前景。",
            "图书馆报告厅",
            LocalDateTime.now().plusDays(7),
            LocalDateTime.now().plusDays(7).plusHours(2),
            200,
            ActivityStatus.UPCOMING
        );
        
        Activity act3 = createActivity(
            "校园歌手大赛初赛",
            "展现你的音乐才华，用歌声点亮青春！初赛选出前20名进入决赛。",
            "大礼堂",
            LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(10).plusHours(4),
            100,
            ActivityStatus.UPCOMING
        );
        
        Activity act4 = createActivity(
            "志愿者招募：校园环保行动",
            "为建设绿色校园贡献力量，参与校园环境整治和环保宣传活动。",
            "校园各区域",
            LocalDateTime.now().plusDays(5),
            LocalDateTime.now().plusDays(5).plusHours(6),
            80,
            ActivityStatus.UPCOMING
        );
        
        Activity act5 = createActivity(
            "创新创业大赛",
            "鼓励学生创新思维，展示创业项目，优秀项目可获得资金和导师支持。",
            "创新中心",
            LocalDateTime.now().plusDays(20),
            LocalDateTime.now().plusDays(20).plusHours(5),
            150,
            ActivityStatus.UPCOMING
        );
        
        Activity act6 = createActivity(
            "迎新晚会",
            "精彩的文艺表演，欢迎新同学加入我们的大家庭！",
            "体育馆",
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(10).plusHours(3),
            1000,
            ActivityStatus.COMPLETED
        );
        
        // Create some registrations
        createRegistration(user1, act1);
        createRegistration(user1, act2);
        createRegistration(user2, act1);
        createRegistration(user2, act3);
        createRegistration(student1, act2);
        createRegistration(student2, act4);
        
        log.info("Database initialization completed successfully!");
        log.info("Created 5 users (1 admin, 4 students), 6 activities, and 6 registrations");
        log.info("Default accounts - admin/123456, student1/123456, student2/123456");
    }
    
    private User createUser(String username, String password, String studentId, String email, String phone, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStudentId(studentId);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        return userRepository.save(user);
    }
    
    private Activity createActivity(String title, String description, String location,
                                   LocalDateTime startTime, LocalDateTime endTime,
                                   int maxParticipants, ActivityStatus status) {
        Activity activity = new Activity();
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setLocation(location);
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setMaxParticipants(maxParticipants);
        activity.setCurrentParticipants(0);
        activity.setStatus(status);
        return activityRepository.save(activity);
    }
    
    private void createRegistration(User user, Activity activity) {
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setActivity(activity);
        registration.setStatus(Registration.RegistrationStatus.ACTIVE);
        registrationRepository.save(registration);
        
        // Update activity participant count
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activityRepository.save(activity);
    }
}
