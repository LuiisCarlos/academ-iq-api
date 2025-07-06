package dev.luiiscarlos.academ_iq_api.shared.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.identity.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminContext {

    private final UserFacade userFacade;

    /**
     *
     * @return
     */
    public String admin() {
        return userFacade.get((long) SecurityContextHolder.getContext()
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
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
