package moinammaoueni.kmtech.api.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySlug(String slug);

    Optional<User> findByEmail(String email);

    boolean existsBySlug(String slug);

    boolean existsByEmail(String email);
}
