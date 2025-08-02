package com.example.webblog.controller;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.request.PostFilterRequest;
import com.example.webblog.dto.request.PostUpdateRequest;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.service.Post.PostService;
import com.example.webblog.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody PostCreateRequest request) {
        return ResponseHelper.success(postService.createPost(request), "Tao post thanh cong");
    }

    @DeleteMapping("/{idpost}")
    public ResponseEntity<ApiResponse<Object>> deletePost(@PathVariable("idpost") String id) {
        postService.deletePost(id);
        return ResponseHelper.success("Delete post success");
    }

    @PatchMapping("/{idpost}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable("idpost") String id, @RequestBody PostUpdateRequest request) {
        return ResponseHelper.success(postService.updatePost(id,request), "Cap nhat thong tin bai viet thanh cong");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostResponse>> getPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PostFilterRequest postFilterRequest = PostFilterRequest.builder()
                .category(category)
                .keyword(keyword)
                .email(email)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        return ResponseHelper.ofPage(postService.getPosts(postFilterRequest,  page, pageSize));
    }
}
