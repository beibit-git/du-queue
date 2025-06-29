package kz.dulaty.queue.feature.data.entity.auth;

import jakarta.persistence.*;
import kz.dulaty.queue.feature.data.enums.SafetyRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SafetyRole safetyRole;

    @Override
    public String getAuthority() {
        return this.safetyRole.name();
    }
}
