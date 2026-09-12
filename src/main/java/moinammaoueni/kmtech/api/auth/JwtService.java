package moinammaoueni.kmtech.api.auth;

public interface JwtService {

    String generateToken(String email, String role);

    String extractEmail(String token);

    boolean isTokenValid(String token, String email);

    long getExpirationTime();
}