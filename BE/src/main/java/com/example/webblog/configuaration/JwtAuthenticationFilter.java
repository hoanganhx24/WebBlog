package com.example.webblog.configuaration;

import com.example.webblog.service.Auth.AuthService;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicEndpoint(requestURI, method)) {
            log.info("Public endpoint requested: " + requestURI + ", method: " + method);
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            try {
                SignedJWT signedJWT = authService.verifyToken(token);

                if (signedJWT != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String username = signedJWT.getJWTClaimsSet().getSubject();
                    String scope = signedJWT.getJWTClaimsSet().getStringClaim("scope");
                    String userId = signedJWT.getJWTClaimsSet().getStringClaim("userId");

                    List<SimpleGrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());

                    // Create custom user details with userId
                    CustomUserDetails userDetails = new CustomUserDetails(username, userId, authorities);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Set authentication for user: {} with userId: {}", username, userId);
                }
            } catch (AuthenticationException e) {
                log.error("JWT authentication failed: {}", e.getMessage());
                request.setAttribute("auth_error", e);
            } catch (ParseException e) {
                log.error("JWT parsing failed: {}", e.getMessage());
                request.setAttribute("auth_error", new AuthenticationException("Invalid JWT format"));
            }
        } else {
            // For protected endpoints without token, set auth error
            log.error("No JWT token found for protected endpoint: {}", requestURI);
            request.setAttribute("auth_error", new AuthenticationException("JWT token is required"));
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private boolean isPublicEndpoint(String  requestURI, String method) {

        log.info("Checking if {} {} is a public endpoint", method, requestURI);

        // Kiểm tra POST endpoints
        if ("POST".equalsIgnoreCase(method)) {
            return Arrays.stream(SecurityConfig.PUBLIC_ENDPOINTS_POST)
                    .anyMatch(endpoint -> matchesEndpoint(requestURI, endpoint));
        }
        // Kiểm tra GET endpoints
        else if ("GET".equalsIgnoreCase(method)) {
            return Arrays.stream(SecurityConfig.PUBLIC_ENDPOINTS_GET)
                    .anyMatch(endpoint -> matchesEndpoint(requestURI, endpoint));
        }

        return false;
    }

    private boolean matchesEndpoint(String requestURI, String endpoint) {
        boolean isMatch = false;

        if (endpoint.endsWith("/**")) {
            String baseEndpoint = endpoint.substring(0, endpoint.length() - 3);
            isMatch = requestURI.startsWith(baseEndpoint);
        } else {
            isMatch = requestURI.equals(endpoint) || requestURI.endsWith(endpoint);
        }

        if (isMatch) {
            log.info("URI '{}' matches public endpoint pattern '{}'", requestURI, endpoint);
        }
        return isMatch;
    }
}
