package co.istad.ishop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.istad.ishop.entities.Token;
import co.istad.ishop.exception.InvalidPasswordException;
import co.istad.ishop.exception.UserNotVerifiedException;
import co.istad.ishop.model.request.AuthRequest;
import co.istad.ishop.model.response.AuthResponse;
import co.istad.ishop.repository.TokenRepository;
import co.istad.ishop.security.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Value("${jwt.access-token.expire}")
    private long accessTokenExpiry;

    @Value("${jwt.refresh-token.expire}")
    private long refreshTokenExpiry;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

            // save tokens to db
            saveToken(accessToken, request.getUsername(), "ACCESS", System.currentTimeMillis() + accessTokenExpiry);
            saveToken(refreshToken, request.getUsername(), "REFRESH", System.currentTimeMillis() + refreshTokenExpiry);

            return new AuthResponse(accessToken, refreshToken, "Bearer");
        } catch (BadCredentialsException e) {
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

        if(!jwtService.isRefreshToken(refreshToken)) {
            throw new AccessDeniedException("This not the refresh token");
        }

        tokenRepository.findByTokenAndValid(refreshToken, true)
                .orElseThrow(() -> new JwtException("Refresh token have been revoked"));

        String username = jwtService.extractUsername(refreshToken);

        // Invalidate old token
        invalidateTokens(username);

        // new generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(username);

        // save new tokens
        saveToken(newAccessToken, username, "ACCESS", System.currentTimeMillis() + accessTokenExpiry);
        saveToken(newRefreshToken, username, "REFRESH", System.currentTimeMillis() + refreshTokenExpiry);

        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer");
    }


    private void saveToken(String token, String username, String type, long expiry) {
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUsername(username);
        tokenEntity.setType(type);
        tokenEntity.setExpiration(expiry);
        tokenEntity.setValid(true);
        tokenRepository.save(tokenEntity);
    }

    private void invalidateTokens(String username) {
        List<Token> tokens = tokenRepository.findByUsernameAndTypeAndValid(username, "ACCESS", true);
        tokens.addAll(tokenRepository.findByUsernameAndTypeAndValid(username, "REFRESH", true));
        for (Token token : tokens) {
            token.setValid(false);
            tokenRepository.save(token);
        }
    }
}

