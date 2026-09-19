package moinammaoueni.kmtech.api.media;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.project.Project;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	java.util.List<Media> findByProjectOrderByCreatedAtAsc(moinammaoueni.kmtech.api.project.Project project);
	Optional<Media> findFirstByProjectOrderByCreatedAtAsc(Project project);
}
