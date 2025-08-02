package com.example.webblog.mapper;

import com.example.webblog.dto.response.PostAuthorResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponse toUserResponse(User user);
    PostAuthorResponse toPostAuthorResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
}
