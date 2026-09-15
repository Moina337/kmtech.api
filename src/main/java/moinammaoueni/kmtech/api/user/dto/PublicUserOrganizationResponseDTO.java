package moinammaoueni.kmtech.api.user.dto;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;

public record PublicUserOrganizationResponseDTO (
		
        String slug,
        String name,
        MediaResponseDTO media
        
) {}