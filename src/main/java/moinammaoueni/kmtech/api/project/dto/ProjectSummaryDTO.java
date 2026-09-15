package moinammaoueni.kmtech.api.project.dto;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;

public record ProjectSummaryDTO(
    String slug,
    String name,
    String description,
    MediaResponseDTO cover,
    String ownerSlug,
    String ownerName,
    String organizationSlug,
    String organizationName
) {}
