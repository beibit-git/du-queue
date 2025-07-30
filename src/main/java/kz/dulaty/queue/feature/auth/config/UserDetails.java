package kz.dulaty.queue.feature.auth.config;

import kz.dulaty.queue.feature.auth.data.enums.SafetyRole;
import kz.dulaty.queue.feature.auth.data.entity.Role;
import kz.dulaty.queue.feature.auth.data.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public record UserDetails(User user) implements org.springframework.security.core.userdetails.UserDetails {
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
        return org.springframework.security.core.userdetails.UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return org.springframework.security.core.userdetails.UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }
}
