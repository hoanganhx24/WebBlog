package com.example.webblog.service;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.AuthResponse;
import com.nimbusds.jwt.SignedJWT;

import javax.naming.AuthenticationException;

public interface AuthService {
    AuthResponse register(RegisterRequets registerDTO);
    AuthResponse login(LoginRequest loginDTO);
    SignedJWT verifyToken(String token) throws AuthenticationException;
}
