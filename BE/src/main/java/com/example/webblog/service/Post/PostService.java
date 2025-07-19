package com.example.webblog.service.Post;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.entity.Post;

public interface PostService {
    PostResponse createPost(PostCreateRequest request);
    void deletePost(String id);
}
