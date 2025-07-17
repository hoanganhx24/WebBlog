package com.example.webblog.service.User;

import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toUserResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id)));
    }

    @Override
    public UserResponse changeInfo(String id, UserChangeRequest req) {
        var user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (!user.getEmail().equals(req.getEmail())) {
            if(userRepository.existsByEmail(req.getEmail())){
                throw new DuplicateResourceException("Email already exists");
            }
            user.setEmail(req.getEmail());
        }
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        var user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
    }
}
