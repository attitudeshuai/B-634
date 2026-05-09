package com.campus.dto;

import com.campus.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private String studentId;
    private User.Role role;
}
