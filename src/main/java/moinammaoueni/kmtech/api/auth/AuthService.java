package moinammaoueni.kmtech.api.auth;

import moinammaoueni.kmtech.api.auth.dto.AuthenticationResponseDTO;
import moinammaoueni.kmtech.api.auth.dto.LoginRequestDTO;
import moinammaoueni.kmtech.api.auth.dto.RegisterRequestDTO;

public interface AuthService {

    AuthenticationResponseDTO register(RegisterRequestDTO request);

    AuthenticationResponseDTO login(LoginRequestDTO request);
    
    void verifyEmail(String token);
    void resendVerification(String email);
}
