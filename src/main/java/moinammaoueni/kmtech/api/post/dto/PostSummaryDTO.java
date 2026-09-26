package moinammaoueni.kmtech.api.post.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryDTO;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

@Builder
public record PostSummaryDTO(

		Long id,
		
		String slug,

		String content,

		MediaResponseDTO cover,


		LocalDateTime create) {

}
