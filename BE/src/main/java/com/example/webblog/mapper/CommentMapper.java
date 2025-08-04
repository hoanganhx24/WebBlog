package com.example.webblog.mapper;

import com.example.webblog.dto.response.CommentResponse;
import com.example.webblog.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    CommentResponse toCommentResponse(Comment comment);
    List<CommentResponse> toCommentResponseList(List<Comment> comments);
}
