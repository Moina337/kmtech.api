package moinammaoueni.kmtech.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import moinammaoueni.kmtech.api.user.dto.ChangePasswordRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public UserResponseDTO findMe() {
        return null;
    }

    @GetMapping("/public/{slug}")
    public UserResponseDTO findPublicBySlug(@PathVariable String slug) {
        return null;
    }

    @PatchMapping("/me")
    public UserResponseDTO updateMe(@Valid @RequestBody UpdateUserRequestDTO request) {
        return null;
    }

    @PatchMapping("/me/password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
    }
}
