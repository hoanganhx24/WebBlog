package com.example.webblog.controller;

import com.example.webblog.dto.request.UserFilterRequest;
import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.PageResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.enums.Role;
import com.example.webblog.service.User.UserService;
import com.example.webblog.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/alluser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUser(){
        List<UserResponse> list = userService.getAllUser();
        return ResponseHelper.success(list, "Lay thong tin thanh cong");
    }

    @GetMapping("/{userid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("userid") String userid){
        UserResponse userRes = userService.getUserById(userid);
        return ResponseHelper.success(userRes, "Lay thong tin user thanh cong");
    }

    @PatchMapping("/{userid}")
    public ResponseEntity<ApiResponse<UserResponse>>  updateUser(@PathVariable("userid") String userid, @RequestBody UserChangeRequest req){
        UserResponse userRes= userService.changeInfo(userid, req);
        return ResponseHelper.success(userRes, "Cap nhat thong tin thanh cong");
    }

    @DeleteMapping("/{iduser}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable("iduser") String iduser){
        userService.deleteUser(iduser);
        return ResponseHelper.success("Xoa thanh cong user");

    }

    @GetMapping("/myInfo")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(){
        UserResponse userRes = userService.getMyInfo();
        return ResponseHelper.success(userRes, "Lay thong tin thanh cong");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role  role,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
            ){
        UserFilterRequest userFilterRequest = UserFilterRequest.builder()
                .keyword(keyword)
                .role(role)
                .isActive(isActive)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
        return ResponseHelper.success(userService.getUsers(userFilterRequest, page, pageSize, sortBy, sortOrder), "Lay danh sach user co phan trang thanh cong");
    }

}
