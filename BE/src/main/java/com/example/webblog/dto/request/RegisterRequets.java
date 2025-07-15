package com.example.webblog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRequets {
    @NotBlank(message = "Username là bắt buộc")
    @Length(min = 2, max = 50, message = "Username có từ 2 cho đến 50 kí tự!")
    private String username;

    @NotEmpty
    @Email
    @Length(min = 10, max = 50 , message = "Email có từ 10 đến 15 kí tự!")
    private String email;

    @NotBlank(message = "Password là bắt buộc!")
    @Length(min = 8, max = 20, message = "Mật khẩu có từ 8 đến 20 kí tự!")
    private String password;

}
