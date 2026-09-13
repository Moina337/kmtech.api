package moinammaoueni.kmtech.api.skill;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.user.User;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findAllByUser(User user);

    List<UserSkill> findAllByUserId(Long userId);

    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    Optional<UserSkill> findByUserIdAndSkillId(Long userId, Long skillId);

    boolean existsBySkillId(Long skillId);

    void deleteByUserIdAndSkillId(Long userId, Long skillId);
}
