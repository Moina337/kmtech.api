package moinammaoueni.kmtech.api.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.user.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findBySlug(String slug);

    Optional<Project> findBySlugAndStatus(String slug, ProjectStatus status);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByUser(User user);

    boolean existsBySlug(String slug);
}
