package com.example.webblog.mapper;

import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.PostAuthorResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.enums.Role;
import com.example.webblog.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(RegisterRequets registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setRole(Role.USER);
        user.setIsActive(true);

        return user;
    }


    public UserResponse toUserResponse(User user) {
        UserResponse userResponseDTO = new UserResponse();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setIsActive(user.getIsActive());
        userResponseDTO.setCreated_At(user.getCreatedAt());
        userResponseDTO.setUpdated_At(user.getUpdatedAt());

        return userResponseDTO;
    }

    public PostAuthorResponse toPostAuthorResponse(User user) {
        PostAuthorResponse postAuthorResponse = PostAuthorResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return postAuthorResponse;
    }

}
