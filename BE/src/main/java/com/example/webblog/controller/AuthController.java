package com.example.webblog.controller;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.LogoutRequest;
import com.example.webblog.dto.request.RefreshRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.AuthResponse;
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

import javax.naming.AuthenticationException;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequets req) {
        AuthResponse authResponse = authService.register(req);
        return ResponseHelper.created(authResponse, "Tạo tài khoản thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse authResponse = authService.login(req);
        return ResponseHelper.success(authResponse, "Dang nhap thanh cong");
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestBody LogoutRequest req) throws AuthenticationException, ParseException {
        authService.logout(req);
        return ResponseHelper.success("Dang xuat thanh cong");
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshRequest req) throws AuthenticationException, ParseException {
        AuthResponse authResponse = authService.refreshToken(req);
        return ResponseHelper.success(authResponse, "Refresh Token thanh cong");
    }
}

