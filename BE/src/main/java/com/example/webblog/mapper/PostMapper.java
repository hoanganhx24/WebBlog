package com.example.webblog.mapper;

import com.example.webblog.dto.response.*;
import com.example.webblog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);
}
