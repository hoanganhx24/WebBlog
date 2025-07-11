package com.example.webblog.service;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.AuthResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.Role;
import com.example.webblog.entity.User;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.UserRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @NonFinal
    @Value("${jwt.signerkey}")
    private String signerKey;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequets registerDTO) {

        //System.out.println("Da chay den day");
        if(userRepository.existsByUsername(registerDTO.getUsername())){
            throw new DuplicateResourceException("Username already exists");
        }

        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new DuplicateResourceException("Email already exists");
        }

        User user = userMapper.toUser(registerDTO);

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        var token = generateToken(user.getUsername());

        userRepository.save(user);

        UserResponse  userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

    }

    @Override
    public AuthResponse login(LoginRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + req.getUsername()));

        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new ValidationException("Mat khau khong chinh xac");
        }
        var token = generateToken(user.getUsername());
        UserResponse  userResponse = userMapper.toUserResponse(user);
        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("hoanganhx24")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("role", Role.USER)
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

       try{
           jwsObject.sign(new MACSigner(signerKey.getBytes()));
           return jwsObject.serialize();
       }
       catch (Exception e){
           throw new ValidationException("JWT signing failed");
       }
    }
}
