package com.example.webblog.configuaration;

import com.example.webblog.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        Object authentication = request.getAttribute("auth_error");

        ApiResponse<?> apiResponse;

        if (authentication instanceof AuthenticationException ex){
            apiResponse = ApiResponse.builder()
                    .message(ex.getMessage())
                    .success(false)
                    .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .errors(List.of(ex.getMessage()))
                    .build();
        }
        else {
            apiResponse = ApiResponse.builder()
                    .message("Token is missing or invalid")
                    .success(false)
                    .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .errors(List.of("Unauthorized"))
                    .build();
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(response.getWriter(), apiResponse);
        response.flushBuffer();
    }
}
