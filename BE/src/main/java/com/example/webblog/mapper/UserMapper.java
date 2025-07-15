package com.example.webblog.mapper;

import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.Role;
import com.example.webblog.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(RegisterRequets registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setRole(Role.USER);
        user.setIsActive(true);

        return user;
    }


    public UserResponse toUserResponse(User user) {
        UserResponse userResponseDTO = new UserResponse();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setIsActive(user.getIsActive());
        userResponseDTO.setCreated_At(user.getCreatedAt());
        userResponseDTO.setUpdated_At(user.getUpdatedAt());

        return userResponseDTO;
    }

}
