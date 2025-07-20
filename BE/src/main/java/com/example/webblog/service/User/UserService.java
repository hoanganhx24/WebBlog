package com.example.webblog.service.User;

import com.example.webblog.dto.request.UserFilterRequest;
import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.PageResponse;
import com.example.webblog.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUser();
    UserResponse getUserById(String id);
    UserResponse changeInfo(String id, UserChangeRequest req);
    UserResponse getMyInfo();
    void deleteUser(String id);
    PageResponse<UserResponse> getUsers(UserFilterRequest request, int page, int pageSize, String sortBy, String sortOrder);
}
