package moinammaoueni.kmtech.api.user;

import moinammaoueni.kmtech.api.user.dto.ChangePasswordRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO findMe();

    UserResponseDTO findPublicBySlug(String slug);

    UserResponseDTO updateMe(UpdateUserRequestDTO request);

    void changePassword(ChangePasswordRequestDTO request);
}
