package moinammaoueni.kmtech.api.organization.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.OrganizationStatus;
import moinammaoueni.kmtech.api.organization.OrganizationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponseDTO {

    private String slug;
    private String name;
    private String description;
    private OrganizationType type;
    private String website;
    private String location;
    private MediaResponseDTO media;
    private OrganizationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
