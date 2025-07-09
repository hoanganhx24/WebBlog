package com.example.webblog.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO {
    Long id;
    String username;
    String email;
    String fullname;
    Integer role;
    Integer isActive;
    LocalDateTime created_At;
    LocalDateTime updated_At;
}
