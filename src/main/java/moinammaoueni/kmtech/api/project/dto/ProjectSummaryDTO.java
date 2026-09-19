package moinammaoueni.kmtech.api.project.dto;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationSummaryDTO;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

public record ProjectSummaryDTO(


	    String slug,

	    String name,

	    String description,

	    MediaResponseDTO cover,

	    UserSummaryDTO user,

	    OrganizationSummaryDTO organization

	) {}
