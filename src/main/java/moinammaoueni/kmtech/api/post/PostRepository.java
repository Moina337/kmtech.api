package moinammaoueni.kmtech.api.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.user.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Post> findByUser(User user);

    List<Post> findByOrganization(Organization organization);

    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findByIdAndUser(Long postId, User user);

    Optional<Post> findByIdAndOrganization(Long postId, Organization organization);
}
