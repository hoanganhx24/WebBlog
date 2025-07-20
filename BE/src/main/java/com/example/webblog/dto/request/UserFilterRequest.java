package com.example.webblog.dto.request;

import com.example.webblog.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserFilterRequest {
    String keyword;
    Role role;
    Boolean isActive;
    LocalDateTime fromDate;
    LocalDateTime toDate;
}
