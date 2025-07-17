package com.example.webblog.dto.response;

import com.example.webblog.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CategoryCreateResponse {
    String id;
    String name;
    String slug;
    Category categoryParent;
}
