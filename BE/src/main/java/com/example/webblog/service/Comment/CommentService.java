package com.example.webblog.service.Comment;

import com.example.webblog.dto.request.CommentCreateRequest;
import com.example.webblog.dto.request.CommentUpdateRequest;
import com.example.webblog.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentResponse createComment(CommentCreateRequest request);
    Page<CommentResponse> getComments(String postId, int page, int pageSize);
    CommentResponse updateComment(CommentUpdateRequest request, String idComment);
    void deleteComment(String idComment);
}
