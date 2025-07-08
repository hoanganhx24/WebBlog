package com.example.webblog.controller;

import com.example.webblog.dto.request.UserChangeDTO;
import com.example.webblog.dto.response.UserResponseDTO;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.model.response.SuccessResponse;
import com.example.webblog.service.UserService;
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
    public ResponseEntity<SuccessResponse<List<UserResponseDTO>>> getAllUser(){
        return ResponseEntity.ok(new SuccessResponse<>(200, userService.getAllUser()));
    }

    @GetMapping("/{userid}")
    public ResponseEntity<SuccessResponse<UserResponseDTO>> getUserById(@PathVariable("userid") Long userid){
        return ResponseEntity.ok(new SuccessResponse<>(200, userService.getUserById(userid)));
    }

    @PatchMapping("/{userid}")
    public ResponseEntity<SuccessResponse<UserResponseDTO>>  updateUser(@PathVariable("userid") Long userid, @RequestBody UserChangeDTO req){
        return ResponseEntity.ok(new SuccessResponse<>(200, userService.changeInfo(userid, req)));
    }

}
