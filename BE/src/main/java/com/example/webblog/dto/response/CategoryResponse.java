package com.example.webblog.dto.response;

import com.example.webblog.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CategoryResponse {
    @NotBlank
    String id;
    @NotBlank
    String name;
    @NotBlank
    String slug;
}
