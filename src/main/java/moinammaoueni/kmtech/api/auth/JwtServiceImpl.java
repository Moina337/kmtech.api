package moinammaoueni.kmtech.api.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.config.JwtProperties;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateToken(String email, String role) {

        Date now = new Date();

        Date expiryDate = new Date(
                now.getTime() + jwtProperties.getExpiration()
        );

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token, String email) {

        try {
            Claims claims = getClaims(token);

            String tokenEmail = claims.getSubject();
            Date expiration = claims.getExpiration();

            return email.equals(tokenEmail)
                    && expiration.after(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long getExpirationTime() {
        return jwtProperties.getExpiration();
    }

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}