package moinammaoueni.kmtech.api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSummaryDTO {

    private String slug;
    private String name;
    private String profileImage;
}