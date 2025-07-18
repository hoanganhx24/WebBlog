package com.example.webblog.mapper;

import com.example.webblog.dto.response.*;
import com.example.webblog.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostMapper {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    UserMapper userMapper;

    public PostResponse toPostResponse(Post post) {
        List<AttachmentResponse> attachmentResponses = new ArrayList<>();
        if (post.getAttachments() != null) {
            attachmentResponses = post.getAttachments()
                    .stream()
                    .map(attachment -> attachmentMapper.toAttachmentResponse(attachment))
                    .toList();
        }
        int commentResponses = 0;
        if (post.getComments() != null) {
            commentResponses = post.getComments().toArray().length;
        }
        List<CategoryResponse> categoryResponses = post.getCategories()
                .stream()
                .map(category -> categoryMapper.toCategoryResponse(category))
                .toList();

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .slug(post.getSlug())
                .status(post.getStatus())
                .categories(categoryResponses)
                .attachments(attachmentResponses)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(commentResponses)
                .build();
        return postResponse;
    }

    public PostFilterResponse toPostFilterResponse(Post post) {
        List<AttachmentResponse> attachmentResponses = new ArrayList<>();
        if (post.getAttachments() != null) {
            attachmentResponses = post.getAttachments()
                    .stream()
                    .map(attachment -> attachmentMapper.toAttachmentResponse(attachment))
                    .toList();
        }
        int commentResponses = 0;
        if (post.getComments() != null) {
            commentResponses = post.getComments().toArray().length;
        }
        List<CategoryResponse> categoryResponses = post.getCategories()
                .stream()
                .map(category -> categoryMapper.toCategoryResponse(category))
                .toList();

        PostFilterResponse postFilterResponse = PostFilterResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .slug(post.getSlug())
                .status(post.getStatus())
                .author(userMapper.toPostAuthorResponse(post.getAuthor()))
                .categories(categoryResponses)
                .attachments(attachmentResponses)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(commentResponses)
                .build();
        return postFilterResponse;
    }
}
