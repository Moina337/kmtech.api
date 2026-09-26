package moinammaoueni.kmtech.api.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryDTO;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

public record PostSummaryPublic (

		String slug,

		String content,

		List<MediaResponseDTO> medias,
		
		UserSummaryDTO user,
		
		OrganizationSummaryDTO organization,

		LocalDateTime create) {

}