package kz.dulaty.queue.feature.auth.service.impl;

import kz.dulaty.queue.feature.auth.data.entity.Role;
import kz.dulaty.queue.feature.auth.data.enums.SafetyRole;
import kz.dulaty.queue.feature.auth.service.RoleService;
import kz.dulaty.queue.feature.data.repository.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByType(SafetyRole safetyRole) {
        return roleRepository.findBySafetyRole(safetyRole)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setSafetyRole(safetyRole);
                    return roleRepository.save(newRole);
                });
    }
}
