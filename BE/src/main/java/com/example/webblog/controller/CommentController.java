package com.example.webblog.controller;

import com.example.webblog.dto.request.CommentCreateRequest;
import com.example.webblog.dto.request.CommentUpdateRequest;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.CommentResponse;
import com.example.webblog.service.Comment.CommentService;
import com.example.webblog.util.ResponseHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@RequestBody CommentCreateRequest request){
        return ResponseHelper.success(commentService.createComment(request), "Tao binh luan thanh cong");
    }

    @PutMapping("/{idComment}")
    public ResponseEntity<ApiResponse<CommentResponse>>
                        updateComment(@PathVariable String idComment,
                                      @RequestBody CommentUpdateRequest request){
        return ResponseHelper.success(commentService.updateComment(request, idComment), "Cap nhap noi dung binh luan thanh cong");
    }

    @DeleteMapping("{idComment}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable String idComment){
        commentService.deleteComment(idComment);
        return ResponseHelper.success("Xoa binh luan thanh cong");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            @RequestParam(required = false) String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ResponseHelper.ofPage(commentService.getComments(postId, page, pageSize));
    }
}
