package moinammaoueni.kmtech.api.organizationmember.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationMemberResponseDTO {

    private String userSlug;
    private String userName;
    private OrganizationMemberRole role;
    private LocalDateTime joinedAt;
}
