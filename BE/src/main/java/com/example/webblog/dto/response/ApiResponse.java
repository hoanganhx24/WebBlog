package com.example.webblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    boolean success;
    String message;
    T data;
    String timestamp;
    int statusCode;
    Map<String, Object> metadata;
    List<String> errors;

    public static <T> ApiResponse<T> ofPage(Page<T> pageResult) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("successfully");
        response.setData((T) pageResult.getContent());
        response.setTimestamp(java.time.Instant.now().toString());
        response.setStatusCode(200);

        // Adding metadata
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("page", pageResult.getNumber());
        metadata.put("size", pageResult.getSize());
        metadata.put("totalPages", pageResult.getTotalPages());
        metadata.put("totalElements", pageResult.getTotalElements());
        metadata.put("first", pageResult.isFirst());
        metadata.put("last", pageResult.isLast());
        metadata.put("hasNext", pageResult.hasNext());
        metadata.put("hasPrevious", pageResult.hasPrevious());
        response.setMetadata(metadata);

        return response;
    }
}
