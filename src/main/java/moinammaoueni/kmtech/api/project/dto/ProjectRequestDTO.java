package moinammaoueni.kmtech.api.project.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDTO(
    @NotBlank
    String name,

    String description,

    String website,

    String github,

    Long organizationId
) {}
