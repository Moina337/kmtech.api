package moinammaoueni.kmtech.api.user;

import org.springframework.web.multipart.MultipartFile;

import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.user.dto.ChangePasswordRequestDTO;
import moinammaoueni.kmtech.api.user.dto.PublicUserResponseDTO;
import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO findMe();

    PublicUserResponseDTO findPublicBySlug(String slug);

    UserResponseDTO updateMe(UpdateUserRequestDTO request);
    
    MediaResponseDTO updateProfilePhoto(MultipartFile file);

    void changePassword(ChangePasswordRequestDTO request);
}
