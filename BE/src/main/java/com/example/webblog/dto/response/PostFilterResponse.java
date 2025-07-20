package com.example.webblog.dto.response;

import com.example.webblog.enums.PostStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostFilterResponse {
    String id;
    String title;
    String content;
    String slug;
    PostStatus status;
    PostAuthorResponse author;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<AttachmentResponse> attachments;
    List<CategoryResponse> categories;
    int comments;
}
