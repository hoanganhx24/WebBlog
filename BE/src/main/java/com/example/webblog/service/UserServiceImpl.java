package com.example.webblog.service;

import com.example.webblog.dto.request.UserChangeDTO;
import com.example.webblog.dto.response.UserResponseDTO;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserResponseDTO> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toUserResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(long id) {
        return userMapper.toUserResponse(userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id)));
    }

    @Override
    public UserResponseDTO changeInfo(Long id, UserChangeDTO req) {
        var user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (!user.getEmail().equals(req.getEmail())) {
            if(userRepository.existsByEmail(req.getEmail())){
                throw new DuplicateResourceException("Email already exists");
            }
            user.setEmail(req.getEmail());
        }
        if (user.getFullname().equals(req.getFullname())) {
            user.setFullname(req.getFullname());
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
