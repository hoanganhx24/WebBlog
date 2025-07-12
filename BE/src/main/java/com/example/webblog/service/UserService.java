package com.example.webblog.service;

import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUser();
    UserResponse getUserById(long id);
    UserResponse changeInfo(Long id, UserChangeRequest req);
    UserResponse getMyInfo();
    void deleteUser(Long id);
}
