package moinammaoueni.kmtech.api.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;



@Builder
public record PostResponse(
		
		Long id,

		String content, 
		
		 List<MediaResponseDTO> media,

		LocalDateTime create
		
		
		) {}