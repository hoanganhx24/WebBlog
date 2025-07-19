package com.example.webblog.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostUpdateRequest {
    String title;

    String content;

    List<String> categoryIds;

    List<AttachmentRequest> attachments;
}
