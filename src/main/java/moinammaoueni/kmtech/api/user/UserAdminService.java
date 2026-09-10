package moinammaoueni.kmtech.api.user;

import java.util.List;

import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

public interface UserAdminService {

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Long id);

    UserResponseDTO changeUserStatus(Long id, User.Status status);

    UserResponseDTO changeUserRole(Long id, User.Role role);
}
