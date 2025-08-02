package com.example.webblog.service.Auth;

import com.example.webblog.dto.request.LoginRequest;
import com.example.webblog.dto.request.LogoutRequest;
import com.example.webblog.dto.request.RefreshRequest;
import com.example.webblog.dto.request.RegisterRequets;
import com.example.webblog.dto.response.AuthResponse;
import com.example.webblog.entity.InvalidateToken;
import com.example.webblog.entity.User;
import com.example.webblog.enums.Role;
import com.example.webblog.exception.DuplicateResourceException;
import com.example.webblog.repository.InvalidateTokenRepository;
import com.example.webblog.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.ValidationException;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

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

    private final InvalidateTokenRepository invalidateTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           InvalidateTokenRepository invalidateTokenRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.invalidateTokenRepository = invalidateTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public AuthResponse register(RegisterRequets registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new DuplicateResourceException("Email already exists");
        }
        User user = User.builder()
                .email(registerDTO.getEmail())
                .role(Role.USER)
                .isActive(true)
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();
        userRepository.save(user);

        return generateAuthResponse(user);

    }

    @Override
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);

        if(Objects.isNull(user)){
            throw new ValidationException("User not found with email: " + req.getEmail());

        }
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new ValidationException("Mat khau khong chinh xac");
        }
        return generateAuthResponse(user);
    }

    @Override
    public SignedJWT verifyToken(String token) throws AuthenticationException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            boolean isSignatureValid = signedJWT.verify(verifier);
            if (!isSignatureValid) {
                throw new AuthenticationException("Invalid JWT signature");
            }

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if ((expiryTime == null || !expiryTime.after(new Date()))) {
                throw new AuthenticationException("Expired JWT token");
            }

            if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw  new AuthenticationException("Token has expired");
            }

            return signedJWT;
        } catch (ParseException e) {
            throw new AuthenticationException("Invalid JWT format");
        } catch (JOSEException e) {
            throw new AuthenticationException("JWT verification failed");
        }
    }


    @Override
    public void logout(LogoutRequest request) throws AuthenticationException {
        SignedJWT signed = verifyToken(request.getToken());
        try {
            saveInvalidatedToken(signed);
        }
        catch (ParseException e){
            throw new AuthenticationException("Invalid JWT format");
        }
    }


    @Override
    public AuthResponse refreshToken(RefreshRequest request) throws AuthenticationException {
        SignedJWT signed = verifyToken(request.getToken());

        try {
            String email = signed.getJWTClaimsSet().getSubject();

            User existingUser = userRepository.findByEmail(email)
                    .orElse(null);
            if (Objects.isNull(existingUser)) {
                throw new AuthenticationException("Subject token invalid");
            }

            saveInvalidatedToken(signed);
            return generateAuthResponse(existingUser);

        }
        catch (ParseException e){
            throw new AuthenticationException("Invalid JWT format");
        }
    }



    private String generateToken(User user, boolean isRefreshToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("web-blog")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus((isRefreshToken) ? refreshableDuration : validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", (user.getRole() != null) ? user.getRole().toString() : "USER")
                .claim("userId", user.getId())
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("The code has an error");
        }
    }

    private AuthResponse generateAuthResponse(User user) {
        return AuthResponse.builder()
                .authenticated(true)
                .accessToken(generateToken(user, false))
                .refreshToken(generateToken(user, true))
                .build();
    }

    private void saveInvalidatedToken(SignedJWT signedJWT) throws ParseException {
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();

        invalidateTokenRepository.save(invalidateToken);
    }


}
