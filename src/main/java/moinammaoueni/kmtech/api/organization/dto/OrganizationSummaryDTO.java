package moinammaoueni.kmtech.api.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class OrganizationSummaryDTO {

    private String slug;
    private String name;
    private String logo;
}