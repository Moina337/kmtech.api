package moinammaoueni.kmtech.api.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.organization.OrganizationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private OrganizationType type;

    private String website;

    private String location;
}
