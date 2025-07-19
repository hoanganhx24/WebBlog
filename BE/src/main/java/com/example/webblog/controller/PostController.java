package com.example.webblog.controller;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.service.Post.PostService;
import com.example.webblog.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
