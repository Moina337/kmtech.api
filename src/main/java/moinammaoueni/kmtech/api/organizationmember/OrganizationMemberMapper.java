package moinammaoueni.kmtech.api.organizationmember;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.organizationmember.dto.OrganizationMemberResponseDTO;
import moinammaoueni.kmtech.api.organizationmember.dto.PublicOrganisationMembre;
import moinammaoueni.kmtech.api.user.UserOrganizationResponseDTO;
import moinammaoueni.kmtech.api.user.dto.PublicUserOrganizationResponseDTO;

@Mapper(componentModel = "spring")
public interface OrganizationMemberMapper {

    @Mapping(target = "userSlug", source = "user.slug")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "media", source = "user.media")
    OrganizationMemberResponseDTO toResponseDTO(
            OrganizationMember organizationMember
    );

    @Mapping(target = "userSlug", source = "user.slug")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "media", source = "user.media")
    PublicOrganisationMembre toPublicResponseDTO(
            OrganizationMember organizationMember
    );

    List<OrganizationMemberResponseDTO> toResponseDTOs(
            List<OrganizationMember> organizationMembers
    );
    
    @Mapping(target = "slug", source = "organization.slug")
    @Mapping(target = "name", source = "organization.name")
    @Mapping(target = "media", source = "organization.media")
    UserOrganizationResponseDTO toUserOrganizationResponseDTO(
            OrganizationMember organizationMember
    );
    
    @Mapping(target = "slug", source = "organization.slug")
    @Mapping(target = "name", source = "organization.name")
    @Mapping(target = "media", source = "organization.media")
    PublicUserOrganizationResponseDTO toPublicUserOrganizationResponseDTO(
			OrganizationMember organizationMember
	);
}