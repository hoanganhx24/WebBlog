package com.example.webblog.service;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.entity.User;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional
    public User register(RegisterRequets registerDTO) {

        //System.out.println("Da chay den day");
        if(userRepository.existsByUsername(registerDTO.getUsername())){
            throw new DuplicateResourceException("Username already exists");
        }

        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new DuplicateResourceException("Email already exists");
        }

        User user = userMapper.toUser(registerDTO);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));


        return userRepository.save(user);
    }

    @Override
    public User login(LoginRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + req.getUsername()));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new ValidationException("Mat khau khong chinh xac");
        }
        return user;
    }
}
