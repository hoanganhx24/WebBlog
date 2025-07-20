package com.example.webblog.service.Auth;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.LogoutRequest;
import com.example.webblog.dto.request.RefreshRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.AuthResponse;
import com.nimbusds.jwt.SignedJWT;

import javax.naming.AuthenticationException;
import java.text.ParseException;

public interface AuthService {
    AuthResponse register(RegisterRequets registerDTO);
    AuthResponse login(LoginRequest loginDTO);
    SignedJWT verifyToken(String token) throws AuthenticationException;
    void logout(LogoutRequest req) throws AuthenticationException, ParseException;
    AuthResponse refreshToken(RefreshRequest req) throws AuthenticationException, ParseException;
}
