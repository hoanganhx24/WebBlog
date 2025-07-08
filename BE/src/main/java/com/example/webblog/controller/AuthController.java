package com.example.webblog.controller;

import com.example.webblog.dto.request.LoginDTO;
import com.example.webblog.dto.request.RegisterDTO;
import com.example.webblog.dto.response.UserResponseDTO;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.model.response.SuccessResponse;
import com.example.webblog.service.AuthService;
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
    public ResponseEntity<SuccessResponse<UserResponseDTO>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new SuccessResponse<>(200,
                userMapper.toUserResponse(authService.register(registerDTO))));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(new SuccessResponse<>(201,
                userMapper.toUserResponse(authService.login(loginDTO))));
    }
}

