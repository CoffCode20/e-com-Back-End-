package co.istad.ishop.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final KeyUtil keyUtil;

    @Value("${jwt.access-token.expire}")
    private long accessTokenExpiry;

    @Value("${jwt.refresh-token.expire}")
    private long refreshTokenExpiry;

    public JwtService(KeyUtil keyUtil) {
        this.keyUtil = keyUtil;
    }

    public String generateAccessToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .claim("token_type", "access_token")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(keyUtil.getPrivateKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("token_type", "refresh_token")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
                .signWith(keyUtil.getPrivateKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(keyUtil.getPublicKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(keyUtil.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getTokenType(String token) {
        return Jwts.parser()
                .verifyWith(keyUtil.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("token_type", String.class);
    }

    public boolean isAccessToken(String token) {
        return "access_token".equals(getTokenType(token));
    }
    public boolean isRefreshToken(String token) {
        return "refresh_token".equals(getTokenType(token));
    }

}