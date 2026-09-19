
package moinammaoueni.kmtech.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.user.dto.PublicUserResponseDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;
import moinammaoueni.kmtech.api.user.dto.UserSummaryDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);
    
    @Mapping(target = "profileImage", source = "media.url")
    UserSummaryDTO toSummaryDTO(User user);

    PublicUserResponseDTO toPublicResponseDTO(User user);
}

