package com.example.webblog.exception;

import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.util.ResponseHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi 404 - Không tìm thấy tài nguyên
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseHelper.notFound(ex.getMessage());
    }

    // Xử lý lỗi 400 - Dữ liệu đầu vào không hợp lệ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseHelper.badRequest("Validation failed", errors);
    }

    // Lỗi dữ liệu đã tồn tại
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseHelper.conflict(ex.getMessage());
    }

    // Xử lý EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseHelper.notFound(ex.getMessage());
    }

    // 401 - Xác thực thất bại (token invalid/expired)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseHelper.unauthorized(ex.getMessage());
    }

    // 403 - Không có quyền truy cập (valid token nhưng thiếu quyền)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseHelper.forbidden(ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return ResponseHelper.forbidden(ex.getMessage());
    }

    // Xử lý lỗi chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ex.printStackTrace(); // để log lỗi ra console
        return ResponseHelper.internalError("An unexpected error occurred: " + ex.getMessage());
    }
}