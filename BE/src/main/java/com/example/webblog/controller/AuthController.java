package com.example.webblog.controller;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.service.AuthService;
import com.example.webblog.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequets req) {
        UserResponse userResponse = userMapper.toUserResponse(authService.register(req));
        return ResponseHelper.created(userResponse, "Tạo tài khỏ thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest req) {
        UserResponse userResponse = userMapper.toUserResponse(authService.login(req));
        return ResponseHelper.success(userResponse, "Dang nhap thanh cong");
    }
}

