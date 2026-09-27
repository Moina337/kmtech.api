package moinammaoueni.kmtech.api.organizationmember.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.organization.OrganizationStatus;
import moinammaoueni.kmtech.api.organization.OrganizationType;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMemberRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyOrganizationResponseDTO {

	private Long id;
    private String slug;
    private String name;
    private OrganizationType type;
    private OrganizationStatus status;
    private OrganizationMemberRole role;
    private LocalDateTime joinedAt;
}
