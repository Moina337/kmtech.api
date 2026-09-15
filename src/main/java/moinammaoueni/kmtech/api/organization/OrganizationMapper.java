package moinammaoueni.kmtech.api.organization;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.organization.dto.OrganizationPublicResponseDTO;
import moinammaoueni.kmtech.api.organization.dto.OrganizationResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.OrganizationMember;

@Mapper(componentModel = "spring", uses = MediaMapper.class)
public interface OrganizationMapper {

    @Mapping(target = "media", source = "media")
    OrganizationResponseDTO toResponseDTO(Organization organization);
    
    OrganizationPublicResponseDTO toPublicResponseDTO(
            Organization organization,
            List<OrganizationMember> members
    );
}
