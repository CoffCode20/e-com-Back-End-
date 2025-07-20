package co.istad.ishop.service.impl;

import co.istad.ishop.exception.InvalidPasswordException;
import co.istad.ishop.exception.UserNotVerifiedException;
import co.istad.ishop.model.request.AuthRequest;
import co.istad.ishop.model.response.AuthResponse;
import co.istad.ishop.security.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    public AuthResponse login(AuthRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

            return new AuthResponse(accessToken, refreshToken, "Bearer");
        } catch (BadCredentialsException e) {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            throw new InvalidPasswordException("Invalid password");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Username not found");
        } catch (InternalAuthenticationServiceException e) {
            if (e.getCause() instanceof UserNotVerifiedException userNotVerifiedException) {
                throw userNotVerifiedException;
            }
            throw e;
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new JwtException("Refresh token is invalid");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return new AuthResponse(newAccessToken, refreshToken, "Bearer");
    }
}

