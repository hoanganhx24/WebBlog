package com.example.webblog.dto.request;

import com.example.webblog.entity.Role;
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
    int page = 0;
    int pageSize = 10;
    String sortBy;
    String sortOrder;
}
