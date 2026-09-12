package moinammaoueni.kmtech.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponseDTO {

    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private String slug;

    private String name;

    private String email;
}
