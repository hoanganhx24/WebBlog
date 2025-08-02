package com.example.webblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangeRequest {
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    String nickname;
    @NotNull
    LocalDateTime dateOfBirth;
    @NotBlank
    String avatar;
}
