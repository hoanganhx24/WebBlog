package com.example.webblog.service;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.entity.User;

public interface AuthService {
    User register(RegisterRequets registerDTO);
    User login(LoginRequest loginDTO);
}
