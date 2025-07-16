package com.example.webblog.dto.response;

import com.example.webblog.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    String firstName;
    String lastName;
    Role role;
    Boolean isActive;
    LocalDateTime created_At;
    LocalDateTime updated_At;
}
