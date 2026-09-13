package moinammaoueni.kmtech.api.organization;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;

@Mapper(componentModel = "spring", uses = MediaMapper.class)
public interface OrganizationMapper {

    @Mapping(target = "media", source = "media")
    OrganizationResponseDTO toResponseDTO(Organization organization);
}
