package com.example.webblog.service.Post;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.request.PostFilterRequest;
import com.example.webblog.dto.request.PostUpdateRequest;
import com.example.webblog.dto.response.PostResponse;
import org.springframework.data.domain.Page;

public interface PostService {
    PostResponse createPost(PostCreateRequest request);
    void deletePost(String id);
    PostResponse updatePost(String id, PostUpdateRequest request);
    Page<PostResponse> getPosts(PostFilterRequest request, int page, int pageSize);
}
