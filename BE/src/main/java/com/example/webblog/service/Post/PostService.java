package com.example.webblog.service.Post;

import com.example.webblog.dto.request.PostCreateRequest;
import com.example.webblog.dto.request.PostFilterRequest;
import com.example.webblog.dto.request.PostUpdateRequest;
import com.example.webblog.dto.response.PageResponse;
import com.example.webblog.dto.response.PostFilterResponse;
import com.example.webblog.dto.response.PostResponse;
import com.example.webblog.entity.Post;

public interface PostService {
    PostResponse createPost(PostCreateRequest request);
    void deletePost(String id);
    PostResponse updatePost(String id, PostUpdateRequest request);
    PageResponse<PostFilterResponse> getPosts(PostFilterRequest request, int page, int pageSize);
}
