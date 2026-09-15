package moinammaoueni.kmtech.api.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	java.util.List<Media> findByProjectOrderByCreatedAtAsc(moinammaoueni.kmtech.api.project.Project project);
}
