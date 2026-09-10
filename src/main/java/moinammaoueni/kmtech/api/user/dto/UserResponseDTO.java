package moinammaoueni.kmtech.api.user.dto;

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
public class UserResponseDTO {

    private String slug;

    private String name;

    private String email;

    private String bio;

    private String mediaUrl;

    private String location;

    private String website;

    private String github;

    private String linkedin;
}
