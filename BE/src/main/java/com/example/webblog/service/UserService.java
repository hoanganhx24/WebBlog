package com.example.webblog.service;

import com.example.webblog.dto.request.UserChangeDTO;
import com.example.webblog.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUser();
    UserResponseDTO getUserById(long id);
    UserResponseDTO changeInfo(Long id, UserChangeDTO req);
}
