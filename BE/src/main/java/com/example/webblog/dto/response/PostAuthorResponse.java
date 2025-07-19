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
public class PostAuthorResponse {
    String id;
    String username;
    String firstName;
    String lastName;
}
