package com.example.webblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PostCreateRequest {
    @NotBlank
    String title;

    @NotBlank
    String content;

    @NotBlank
    List<String> categoryIds;

    List<AttachmentRequest> attachments;

}
