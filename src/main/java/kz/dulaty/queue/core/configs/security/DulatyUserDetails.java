package kz.dulaty.queue.core.configs.security;

import kz.dulaty.queue.feature.data.entity.auth.Role;
import kz.dulaty.queue.feature.data.entity.auth.User;
import kz.dulaty.queue.feature.data.enums.SafetyRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public record DulatyUserDetails(User user) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    public Set<SafetyRole> getRoleTypes() {
        return user.getRoles().stream().map(Role::getSafetyRole).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }
}
