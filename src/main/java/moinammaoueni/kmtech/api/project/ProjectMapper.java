package moinammaoueni.kmtech.api.project;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.comment.CommentMapper;
import moinammaoueni.kmtech.api.media.Media;
import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.organization.OrganizationMapper;
import moinammaoueni.kmtech.api.project.dto.ProjectManagementResponse;
import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryManagement;
import moinammaoueni.kmtech.api.user.UserMapper;

@Mapper(
	    componentModel = "spring",
	    uses = {
	        MediaMapper.class,
	        UserMapper.class,
	        OrganizationMapper.class,
	        CommentMapper.class
	    }
	)
	public interface ProjectMapper {

	    @Mapping(target = "cover", source = "media")
	    ProjectSummaryDTO toSummaryDTO(Project project);
	    
	    @Mapping(target = "cover", source = "media")
	    ProjectSummaryManagement toSummaryManagementDTO(Project project);

	    ProjectResponseDTO toResponseDTO(Project project);
	    
	    ProjectManagementResponse toManagementProject(Project project);

	    default Media firstMedia(List<Media> media) {
	        if (media == null || media.isEmpty()) {
	            return null;
	        }

	        return media.get(0);
	    }
}
