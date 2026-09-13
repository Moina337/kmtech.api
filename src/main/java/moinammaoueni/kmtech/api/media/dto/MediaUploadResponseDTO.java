package moinammaoueni.kmtech.api.media.dto;

import lombok.Builder;

@Builder
public record MediaUploadResponseDTO(

        Long id,

        String url

) {
}