package moinammaoueni.kmtech.api.media.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import moinammaoueni.kmtech.api.media.MediaType;

@Builder
public record MediaResponseDTO(

		Long id,

		String originalName,
		
		String mimeType,

		MediaType type,

		Long size,

		String url,

		LocalDateTime createdAt

) {
}