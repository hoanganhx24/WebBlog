package com.example.webblog.service.User;

import com.example.webblog.dto.request.UserFilterRequest;
import com.example.webblog.dto.request.UserChangeRequest;
import com.example.webblog.dto.response.PageResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.User;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.Specification.UserSpecification;
import com.example.webblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationServiceException;
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
                .orElseThrow(() -> new AuthenticationServiceException("User not found with username: " + username));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        var user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
    }

    @Override
    public PageResponse<UserResponse> getUsers(UserFilterRequest request) {
        Sort sort = Sort.unsorted();
        if (request.getSortBy() != null) {
            Sort.Direction direction = Sort.Direction.ASC;
            try {
                if (request.getSortOrder() != null && !request.getSortOrder().isEmpty()) {
                    direction = Sort.Direction.fromString(request.getSortOrder());
                }
            }
            catch (IllegalArgumentException e) {
                direction = Sort.Direction.ASC;
            }
            sort = Sort.by(direction, request.getSortBy());
        }

        int page = request.getPage();
        int pageSize = request.getPageSize();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Specification<User> spec = UserSpecification.build(request);
        Page<User> pageResult = userRepository.findAll(spec, pageable);
        List<UserResponse> content = pageResult.getContent()
                .stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
        return PageResponse.<UserResponse>builder()
                .page(request.getPage())
                .size(request.getPageSize())
                .content(content)
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();
    }
}
