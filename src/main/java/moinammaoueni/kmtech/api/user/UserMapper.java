
package moinammaoueni.kmtech.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.user.dto.PublicUserResponseDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    PublicUserResponseDTO toPublicResponseDTO(User user);
}

