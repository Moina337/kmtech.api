package moinammaoueni.kmtech.api.user;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper; // à adapter
    private final CurrentUser currentUser;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        return userMapper.toResponseDTO(getUser(id));
    }

    @Override
    public UserResponseDTO changeUserStatus(Long id, User.Status status) {
    	
        User admin = currentUser.get();

        if (admin.getId().equals(id)) {
            throw new BadRequestException("Vous ne pouvez pas modifier votre propre statut");
        }
        if (status != User.Status.ACTIVE && status != User.Status.INACTIVE) {
            throw new BadRequestException("Statut non autorisé");
        }

        User target = getUser(id);

        if (target.getRole() == User.Role.ADMIN) {
            throw new BadRequestException("Un administrateur ne peut pas être suspendu");
        }

        target.setStatus(status);
        userRepository.save(target);

        log.info("Admin {} a passé l'utilisateur {} en statut {}", admin.getId(), id, status);
        return userMapper.toResponseDTO(target);
    }

    @Override
    public UserResponseDTO changeUserRole(Long id, User.Role role) {
        User admin = currentUser.get();

        if (admin.getId().equals(id)) {
            throw new BadRequestException("Vous ne pouvez pas modifier votre propre rôle");
        }

        User target = getUser(id);
        target.setRole(role);
        userRepository.save(target);

        log.info("Admin {} a changé le rôle de l'utilisateur {} vers {}", admin.getId(), id, role);
        return userMapper.toResponseDTO(target);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }
}