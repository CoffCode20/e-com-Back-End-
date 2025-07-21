package co.istad.ishop.security.jwt;

import co.istad.ishop.exception.ErrorRespond;
import co.istad.ishop.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/api/v1/auth/login") || path.startsWith("/api/v1/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }


        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        // only allow the access token to authentication
//        if(!jwtService.isAccessToken(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String username = jwtService.extractUsername(token);
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtService.isTokenValid(token)) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
        // If there is an Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // ❗️Reject immediately if it's not an access token
            // inside a filter, Spring Security’s internal ExceptionTranslationFilter handles it silently (often with a blank 403 or 401) — and your @ExceptionHandler never sees it.
            if (!jwtService.isAccessToken(token)) {
                sendErrorResponse(response, "Only access_token is support");
            }

//            String username = jwtService.extractUsername(token);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                if (jwtService.isTokenValid(token)) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails, null, userDetails.getAuthorities());
//
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
            try {
                if (!jwtService.isAccessToken(token)) {
                    sendErrorResponse(response, "Only access_token is supported");
                    return;
                }

                if (!jwtService.isTokenValid(token)) {
                    sendErrorResponse(response, "Invalid or expired access_token");
                    return;
                }

                if (tokenRepository.findByTokenAndValid(token, true).isEmpty()) {
                    sendErrorResponse(response, "Access token has been revoked");
                    return;
                }

                String username = jwtService.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (RuntimeException ex) {
                sendErrorResponse(response, ex.getMessage());
                return;
            }
        }

        // Always allow the request to continue
        filterChain.doFilter(request, response);

//        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorRespond<Object> error = ErrorRespond.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(error));
        response.getWriter().flush();
        response.getWriter().close(); // force close writer
    }
}

