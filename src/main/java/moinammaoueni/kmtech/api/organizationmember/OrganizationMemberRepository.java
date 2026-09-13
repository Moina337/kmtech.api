package moinammaoueni.kmtech.api.organizationmember;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.user.User;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {

    Optional<OrganizationMember> findByOrganizationAndUser(Organization organization, User user);

    List<OrganizationMember> findByOrganizationOrderByJoinedAtAsc(Organization organization);

    List<OrganizationMember> findByUserOrderByJoinedAtAsc(User user);

    boolean existsByOrganizationAndUser(Organization organization, User user);

    Optional<OrganizationMember> findByOrganizationAndRole(Organization organization, OrganizationMemberRole role);
}
