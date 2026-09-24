package moinammaoueni.kmtech.api.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.project.Project;
import moinammaoueni.kmtech.api.user.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByAuthor(User author);

    List<Comment> findByAuthorOrderByCreatedAtDesc(User author);

    List<Comment> findByProjectOrderByCreatedAtDesc(Project project);
    
}