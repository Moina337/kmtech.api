package moinammaoueni.kmtech.api.project.dto;

import java.time.LocalDateTime;


import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;

import moinammaoueni.kmtech.api.project.ProjectStatus;


public record ProjectSummaryManagement (
		 
		Long id,
	    String name,
	    String description,
	    ProjectStatus status,
	    String website,
	    String github,
	    MediaResponseDTO cover,
	
	    LocalDateTime createdAt,
	    LocalDateTime updatedAt
		
			) {

	}
