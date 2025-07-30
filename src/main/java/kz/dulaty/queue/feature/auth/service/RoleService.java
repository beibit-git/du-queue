package kz.dulaty.queue.feature.auth.service;

import kz.dulaty.queue.feature.auth.data.entity.Role;
import kz.dulaty.queue.feature.auth.data.enums.SafetyRole;

public interface RoleService {
    Role findByType(SafetyRole safetyRole);
}
