package moinammaoueni.kmtech.api.post.dto;

import java.time.LocalDateTime;
import java.util.List;


import lombok.Builder;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryDTO;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

@Builder
public record PostResponseDTO(

		String slug,

		String content,

		List<MediaResponseDTO> medias,
		
		List<CommentResponseDTO> comments,

		UserSummaryDTO author, 
		
		OrganizationSummaryDTO organization,
		

		LocalDateTime create

		) {

}
