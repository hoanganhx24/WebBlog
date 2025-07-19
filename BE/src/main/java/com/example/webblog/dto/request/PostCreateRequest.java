package com.example.webblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    List<String> categoryIds;

    List<AttachmentRequest> attachments;

}
