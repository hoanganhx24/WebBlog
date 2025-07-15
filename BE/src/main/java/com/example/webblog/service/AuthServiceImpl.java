package com.example.webblog.service;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.LogoutRequest;
import com.example.webblog.dto.request.RefreshRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.AuthResponse;
import com.example.webblog.dto.response.UserResponse;
import com.example.webblog.entity.InvalidateToken;
import com.example.webblog.entity.User;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.mapper.UserMapper;
import com.example.webblog.repository.InvalidateTokenRepository;
import com.example.webblog.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
public class AuthServiceImpl implements AuthService{

    @NonFinal
    @Value("${jwt.signerkey}")
    private String signerKey;

    @NonFinal
    @Value("${jwt.valid.duration}")
    private int validDuration;

    @NonFinal
    @Value("${jwt.refreshable.duration}")
    private int refreshableDuration;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final InvalidateTokenRepository invalidateTokenRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           InvalidateTokenRepository invalidateTokenRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.invalidateTokenRepository = invalidateTokenRepository;
    }


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

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        var token = generateToken(user);

        userRepository.save(user);

        UserResponse  userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

    }

    @Override
    public AuthResponse login(LoginRequest req) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + req.getUsername()));

        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new ValidationException("Mat khau khong chinh xac");
        }
        var token = generateToken(user);
        UserResponse  userResponse = userMapper.toUserResponse(user);
        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws AuthenticationException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            boolean isSignatureValid = signedJWT.verify(verifier);
            if (!isSignatureValid) {
                throw new AuthenticationException("Invalid JWT signature");
            }

            Date expiryTime = (isRefresh)
                    ? new Date(signedJWT.getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli())
                    :signedJWT.getJWTClaimsSet().getExpirationTime();

            if ((expiryTime == null || !expiryTime.after(new Date())) && !isRefresh) {
                throw new AuthenticationException("Expired JWT token");
            }

            if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw  new AuthenticationException("Invalid JWT token");
            }

            return signedJWT;
        } catch (ParseException e) {
            throw new AuthenticationException("Invalid JWT format");
        } catch (JOSEException e) {
            throw new AuthenticationException("JWT verification failed");
        }
    }

    @Override
    public void logout(LogoutRequest req) throws AuthenticationException, ParseException {
        var signed = verifyToken(req.getToken(), false);

        log.info(signed.getJWTClaimsSet().getJWTID());

        String jti = signed.getJWTClaimsSet().getJWTID();
        Date expiryTime = signed.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidateTokenRepository.save(invalidateToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshRequest req) throws AuthenticationException, ParseException {
        var signed = verifyToken(req.getToken(), true);

        String jti = signed.getJWTClaimsSet().getJWTID();
        Date expiryTime = signed.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidateTokenRepository.save(invalidateToken);

        var username = signed.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() ->  new EntityNotFoundException("User not found with username: " + username));

        var token = generateToken(user);
        UserResponse  userResponse = userMapper.toUserResponse(user);
        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("hoanganhx24")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", user.getRole().toString())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

       try{
           jwsObject.sign(new MACSigner(signerKey.getBytes()));
           return jwsObject.serialize();
       }
       catch (Exception e){
           log.error(e.toString());
           throw new ValidationException("JWT signing failed");
       }
    }
}
