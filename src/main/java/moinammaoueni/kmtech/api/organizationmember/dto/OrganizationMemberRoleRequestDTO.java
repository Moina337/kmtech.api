package moinammaoueni.kmtech.api.organizationmember.dto;

import jakarta.validation.constraints.NotNull;
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
public class OrganizationMemberRoleRequestDTO {

    @NotNull
    private OrganizationMemberRole role;
}
