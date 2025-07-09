package com.example.webblog.util;

import com.example.webblog.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseHepler {
    // Success response với data
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(buildResponse(true, message, data, 200, null));
    }

    // Success response không có data (như DELETE)
    public static ResponseEntity<ApiResponse<Object>> success(String message) {
        return ResponseEntity.ok(buildResponse(true, message, null, 200, null));
    }

    // Created response (201)
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(true, message, data, 201, null));
    }

    // Bad Request (400)
    public static ResponseEntity<ApiResponse<Object>> badRequest(String message) {
        return ResponseEntity.badRequest()
                .body(buildResponse(false, message, null, 400, null));
    }

    // Bad Request với validation errors
    public static ResponseEntity<ApiResponse<Object>> badRequest(String message, List<String> errors) {
        return ResponseEntity.badRequest()
                .body(buildResponse(false, message, null, 400, errors));
    }

    // Not Found (404)
    public static ResponseEntity<ApiResponse<Object>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildResponse(false, message, null, 404, null));
    }

    public static ResponseEntity<ApiResponse<Object>> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse(false, message, null, 409, null));
    }

    // Internal Server Error (500)
    public static ResponseEntity<ApiResponse<Object>> internalError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(false, message, null, 500, null));
    }

    // Unauthorized (401)
    public static ResponseEntity<ApiResponse<Object>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildResponse(false, message, null, 401, null));
    }

    // Forbidden (403)
    public static ResponseEntity<ApiResponse<Object>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildResponse(false, message, null, 403, null));
    }

    // Method helper để build response
    private static <T> ApiResponse<T> buildResponse(boolean success, String message,
                                                    T data, int statusCode, List<String> errors) {
        return ApiResponse.<T>builder()
                .success(success)
                .message(message)
                .data(data)
                .statusCode(statusCode)
                .errors(errors)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
