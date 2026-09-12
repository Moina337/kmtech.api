
package moinammaoueni.kmtech.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "mediaUrl", source = "media.url")
    UserResponseDTO toResponseDTO(User user);
}

