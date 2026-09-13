package moinammaoueni.kmtech.api.organizationmember;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;

@Mapper(componentModel = "spring")
public interface OrganizationMemberMapper {

    @Mapping(target = "userSlug", source = "user.slug")
    @Mapping(target = "userName", source = "user.name")
    OrganizationMemberResponseDTO toResponseDTO(OrganizationMember organizationMember);

    List<OrganizationMemberResponseDTO> toResponseDTOs(List<OrganizationMember> organizationMembers);
}
