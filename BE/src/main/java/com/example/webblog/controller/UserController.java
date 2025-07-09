package com.example.webblog.controller;

import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.ApiResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.service.UserService;
import com.example.webblog.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUser(){
        List<UserResponse> list = userService.getAllUser();
        return ResponseHelper.success(list, "Lay thong tin thanh cong");
    }

    @GetMapping("/{userid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable("userid") Long userid){
        UserResponse userRes = userService.getUserById(userid);
        return ResponseHelper.success(userRes, "Lay thong tin user thanh cong");
    }

    @PatchMapping("/{userid}")
    public ResponseEntity<ApiResponse<UserResponse>>  updateUser(@PathVariable("userid") Long userid, @RequestBody UserChangeRequest req){
        UserResponse userRes= userService.changeInfo(userid, req);
        return ResponseHelper.success(userRes, "Cap nhat thong tin thanh cong");
    }

    @DeleteMapping("/{iduser}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable("iduser") Long iduser){
        userService.deleteUser(iduser);
        return ResponseHelper.success("Xoa thanh cong user");

    }

}
