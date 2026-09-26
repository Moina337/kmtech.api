package moinammaoueni.kmtech.api.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.user.dto.ChangePasswordRequestDTO;

import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

    @GetMapping("/me")
    public UserResponseDTO findMe() {
        return userService.findMe();
    }

    @PatchMapping("/me")
    public UserResponseDTO updateMe(@Valid @RequestBody UpdateUserRequestDTO request) {
        return userService.updateMe(request);
    }

    @PatchMapping(value = "/me/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaResponseDTO> updateProfilePhoto(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePhoto(file));
    }

    @PatchMapping("/me/password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        userService.changePassword(request);
    }
}