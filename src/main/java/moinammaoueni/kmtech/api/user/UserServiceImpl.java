package moinammaoueni.kmtech.api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;
import moinammaoueni.kmtech.api.common.exception.BadRequestException;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.media.Media;
import moinammaoueni.kmtech.api.media.MediaMapper;
import moinammaoueni.kmtech.api.media.MediaService;
import moinammaoueni.kmtech.api.media.MediaType;
import moinammaoueni.kmtech.api.media.dto.MediaResponseDTO;
import moinammaoueni.kmtech.api.media.storage.MediaFolder;
import moinammaoueni.kmtech.api.user.dto.ChangePasswordRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UpdateUserRequestDTO;
import moinammaoueni.kmtech.api.user.dto.UserResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final CurrentUser currentUser;
	private final PasswordEncoder passwordEncoder;
	private final MediaService mediaService;
	private final MediaMapper mediaMapper;

	@Override
	@Transactional(readOnly = true)
	public UserResponseDTO findMe() {

		User user = currentUser.get();

		return userMapper.toResponseDTO(user);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDTO findPublicBySlug(String slug) {

		User user = userRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

		return userMapper.toResponseDTO(user);
	}

	@Override
	public UserResponseDTO updateMe(UpdateUserRequestDTO request) {

		User user = currentUser.get();

		user.setName(request.getName());
		user.setBio(request.getBio());
		user.setLocation(request.getLocation());
		user.setWebsite(request.getWebsite());
		user.setGithub(request.getGithub());
		user.setLinkedin(request.getLinkedin());

		User updatedUser = userRepository.save(user);

		return userMapper.toResponseDTO(updatedUser);
	}

	@Override
	public MediaResponseDTO updateProfilePhoto(MultipartFile file) {

		User user = currentUser.get();

		Media media = mediaService.replace(user.getMedia(), file, MediaFolder.USERS, MediaType.PROFILE);
		user.setMedia(media);
		userRepository.save(user);

		return mediaMapper.toMediaResponseDTO(media);
	}

	@Override
	public void changePassword(ChangePasswordRequestDTO request) {

		User user = currentUser.get();

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {

			throw new BadRequestException("Le mot de passe actuel est incorrect");
		}

		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {

			throw new BadRequestException("Le nouveau mot de passe doit être différent de l'ancien");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);
	}
}