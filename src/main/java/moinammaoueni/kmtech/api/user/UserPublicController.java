package moinammaoueni.kmtech.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.user.dto.PublicUserResponseDTO;

@Tag(name = "Utilisateurs (public)", description = "Profils publics des développeurs")
@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserService userService;

    @Operation(summary = "Récupérer le profil public d'un utilisateur par son slug")
    @ApiResponse(responseCode = "200", description = "Profil trouvé")
    @ApiResponse(responseCode = "404", description = "Aucun utilisateur ne correspond à ce slug")
    @GetMapping("/{slug}")
    public PublicUserResponseDTO findPublicBySlug(@PathVariable String slug) {
        return userService.findPublicBySlug(slug);
    }
}