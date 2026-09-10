package moinammaoueni.kmtech.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "media.url", target = "mediaUrl")
    UserResponseDTO userToUserResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDTO(UpdateUserRequestDTO request, @MappingTarget User user);
}
