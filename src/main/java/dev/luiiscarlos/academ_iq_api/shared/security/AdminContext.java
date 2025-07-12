package dev.luiiscarlos.academ_iq_api.shared.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserCrudService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminContext {

    private final UserCrudService UserCrudService;

    /**
     *
     * @return
     */
    public String admin() {
        return UserCrudService.findById((long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();
    }

    /**
     *
     * @return
     */
    public boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

}
