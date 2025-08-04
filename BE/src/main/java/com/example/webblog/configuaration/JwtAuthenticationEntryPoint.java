package com.example.webblog.configuaration;

import com.example.webblog.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        String requestURI = request.getRequestURI();
        Object authError = request.getAttribute("auth_error");

        // Only handle if there's actually an authentication error from our JWT filter
        if (authError == null && authException == null) {
            log.info("No authentication error found, letting exception pass through");
            return;
        }

        ApiResponse<?> apiResponse;
        log.info("Handling authentication error for protected endpoint: {}", requestURI);

        if (authError instanceof AuthenticationException ex){
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
