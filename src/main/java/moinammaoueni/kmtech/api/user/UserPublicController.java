package moinammaoueni.kmtech.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.user.dto.PublicUserResponseDTO;

@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserService userService;

    @GetMapping("/{slug}")
    public PublicUserResponseDTO findPublicBySlug(@PathVariable String slug) {
        return userService.findPublicBySlug(slug);
    }
}