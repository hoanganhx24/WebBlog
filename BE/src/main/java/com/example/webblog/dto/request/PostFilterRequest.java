package com.example.webblog.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostFilterRequest {
    String keyword;
    String category;
    LocalDateTime fromDate;
    LocalDateTime toDate;
}
