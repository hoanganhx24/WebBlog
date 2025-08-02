package com.example.webblog.service.User;

import com.example.webblog.dto.request.UserFilterRequest;
import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.User;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.Specification.UserSpecification;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id)));
    }

    @Override
    public UserResponse changeInfo(String id, UserChangeRequest req) {

        User user = userRepository.findUserById(id).orElse(null);

        if (Objects.isNull(user)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setDob(req.getDateOfBirth());
        user.setNickname(req.getNickname());
        user.setAvatar(req.getAvatar());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationServiceException("User not found with username: " + email));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        var user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserResponse> getUsers(UserFilterRequest request, int page, int pageSize) {
        Sort sort = Sort.unsorted();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<User> spec = UserSpecification.build(request);
        Page<User> pageResult = userRepository.findAll(spec, pageable);
        List<UserResponse> content = userMapper.toUserResponseList(pageResult.getContent());
        return new PageImpl<>(content, pageable, pageResult.getTotalElements());
    }
}
