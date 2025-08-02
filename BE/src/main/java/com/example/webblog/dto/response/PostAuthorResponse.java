package com.example.webblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostAuthorResponse {
    String id;
    String email;
    String nickname;
    String avatar;
    String firstName;
    String lastName;
}
