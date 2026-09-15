package moinammaoueni.kmtech.api.project;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.project.dto.ProjectResponseDTO;
import moinammaoueni.kmtech.api.project.dto.ProjectSummaryDTO;

@Mapper(componentModel = "spring", uses = moinammaoueni.kmtech.api.media.MediaMapper.class)
public interface ProjectMapper {

    @Mapping(target = "media", ignore = true)
    @Mapping(target = "ownerSlug", expression = "java(project.getUser() != null ? project.getUser().getSlug() : null)")
    @Mapping(target = "ownerName", expression = "java(project.getUser() != null ? project.getUser().getName() : null)")
    @Mapping(target = "organizationSlug", expression = "java(project.getOrganization() != null ? project.getOrganization().getSlug() : null)")
    @Mapping(target = "organizationName", expression = "java(project.getOrganization() != null ? project.getOrganization().getName() : null)")
    ProjectResponseDTO toResponseDTO(Project project);

    @Mapping(target = "cover", ignore = true)
    @Mapping(target = "ownerSlug", expression = "java(project.getUser() != null ? project.getUser().getSlug() : null)")
    @Mapping(target = "ownerName", expression = "java(project.getUser() != null ? project.getUser().getName() : null)")
    @Mapping(target = "organizationSlug", expression = "java(project.getOrganization() != null ? project.getOrganization().getSlug() : null)")
    @Mapping(target = "organizationName", expression = "java(project.getOrganization() != null ? project.getOrganization().getName() : null)")
    ProjectSummaryDTO toSummaryDTO(Project project);
}
