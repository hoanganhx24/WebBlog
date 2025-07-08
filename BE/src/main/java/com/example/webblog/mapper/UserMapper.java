package com.example.webblog.mapper;

import com.example.webblog.dto.request.RegisterDTO;
import com.example.webblog.dto.response.UserResponseDTO;
import com.example.webblog.entity.Author;
import com.example.webblog.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setFullname(registerDTO.getFullname());
        user.setPassword(registerDTO.getPassword());
        user.setRole(0);
        user.setIsActive(1);

        if (user.getRole() == 0) {
            Author author = new Author();
            author.setAvatar(null);
            author.setUser(user);
            user.setAuthor(author);
        }
        return user;
    }


    public UserResponseDTO toUserResponse(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFullname(user.getFullname());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setIsActive(user.getIsActive());
        userResponseDTO.setCreated_At(user.getCreatedAt());
        userResponseDTO.setUpdated_At(user.getUpdatedAt());
        userResponseDTO.setAuthor(user.getAuthor());

        return userResponseDTO;
    }

}
