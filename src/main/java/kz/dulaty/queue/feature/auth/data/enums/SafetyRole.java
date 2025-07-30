package kz.dulaty.queue.feature.auth.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SafetyRole {
    ADMIN,
    MODERATOR,
    MANAGER,
    GUEST
}
