package moinammaoueni.kmtech.api.media;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.post.Post;
import moinammaoueni.kmtech.api.project.Project;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	List<Media> findByProjectOrderByCreatedAtAsc(Project project);
	Optional<Media> findFirstByProjectOrderByCreatedAtAsc(Project project);
	
	List<Media> findByPost(Post post);
	
	List<Media> findByPostOrderByCreatedAtDesc(Post post);
}
