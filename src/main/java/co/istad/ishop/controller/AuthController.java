package co.istad.ishop.controller;

import co.istad.ishop.exception.InvalidPasswordException;
import co.istad.ishop.exception.UserNotVerifiedException;
import co.istad.ishop.model.request.AuthRequest;
import co.istad.ishop.model.request.RefreshTokenRequest;
import co.istad.ishop.model.response.BaseResponse;
import co.istad.ishop.model.response.AuthResponse;
import co.istad.ishop.service.RateLimiter;
import co.istad.ishop.service.impl.AuthServiceImpl;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final RateLimiter rateLimiter;

    @PostMapping("/login")
    public BaseResponse<Object> login(@RequestBody AuthRequest request) {
        if (!rateLimiter.resolveBucket(request.getUsername()).tryConsume(1)) {
            return new BaseResponse<>(
                    HttpStatus.TOO_MANY_REQUESTS.toString(),
                    "Too many login attempts",
                    LocalDateTime.now().toString(),
                    null
            );
        }
        try {
            AuthResponse response = authService.login(request);
            return new BaseResponse<>(
                    HttpStatus.OK.toString(),
                    "Login successfully",
                    LocalDateTime.now().toString(),
                    Map.of("details", response)
            );
        } catch (UsernameNotFoundException e) {
            return new BaseResponse<>(
                    HttpStatus.UNAUTHORIZED.toString(),
                    "Authentication failed",
                    LocalDateTime.now().toString(),
                    Map.of("details", "User not found")
            );
        } catch (UserNotVerifiedException e) {
            return new BaseResponse<>(
                    HttpStatus.UNAUTHORIZED.toString(),
                    "Authentication failed",
                    LocalDateTime.now().toString(),
                    Map.of("details", "User not verified")
            );
        } catch (InvalidPasswordException e) {
            return new BaseResponse<>(
                    HttpStatus.UNAUTHORIZED.toString(),
                    "Authentication failed",
                    LocalDateTime.now().toString(),
                    Map.of("details", "Invalid password")
            );
        }
    }

    @PostMapping("/refresh")
    public BaseResponse<Object> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse refreshToken = authService.refreshToken(request.getRefreshToken());
            return new BaseResponse<>(
                    HttpStatus.OK.toString(),
                    "New token generated successfully",
                    LocalDateTime.now().toString(),
                    Map.of("details", refreshToken)
            );
        } catch (JwtException e) {
            throw new JwtException("Invalid refresh token");
        }
    }
}
