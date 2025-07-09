package com.example.webblog.dto.response;

import com.example.webblog.entity.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    String email;
    String fullname;
    Role role;
    Integer isActive;
    LocalDateTime created_At;
    LocalDateTime updated_At;
}
