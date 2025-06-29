package kz.dulaty.queue.feature.data.repository;

import kz.dulaty.queue.feature.data.entity.auth.Role;
import kz.dulaty.queue.feature.data.enums.SafetyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findAllByIdIn(List<Long> idList);
    Optional<Role> findBySafetyRole(SafetyRole safetyRole);
    Boolean existsBySafetyRole(SafetyRole safetyRole);
}