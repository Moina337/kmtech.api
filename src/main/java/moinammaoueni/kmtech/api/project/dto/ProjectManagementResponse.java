package moinammaoueni.kmtech.api.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;

import moinammaoueni.kmtech.api.project.ProjectStatus;


public record ProjectManagementResponse(
		
		Long id,
		String slug,
	    String name,
	    String description,
	    ProjectStatus status,
	    String website,
	    String github,
	    List<MediaResponseDTO> media,
	
	    LocalDateTime createdAt,
	    LocalDateTime updatedAt) {

}
