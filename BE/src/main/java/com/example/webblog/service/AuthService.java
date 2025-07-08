package com.example.webblog.service;

import com.example.webblog.dto.request.LoginDTO;
import com.example.webblog.dto.request.RegisterDTO;
import com.example.webblog.entity.User;

public interface AuthService {
    User register(RegisterDTO registerDTO);
    User login(LoginDTO loginDTO);
}
