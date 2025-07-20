package com.example.webblog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequets {

    @NotEmpty
    @Email
    @Length(min = 10, max = 50 , message = "Email có từ 10 đến 15 kí tự!")
    String email;

    @NotBlank(message = "Password là bắt buộc!")
    @Length(min = 8, max = 20, message = "Mật khẩu có từ 8 đến 20 kí tự!")
    String password;

}
