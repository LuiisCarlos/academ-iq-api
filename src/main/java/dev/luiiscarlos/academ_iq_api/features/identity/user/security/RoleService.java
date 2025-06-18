package dev.luiiscarlos.academ_iq_api.features.identity.user.security;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.identity.auth.exception.RoleNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByAuthority(String authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new RoleNotFoundException(
                        "Failed to find role: Role not found with authority " + authority));
    }

    public Role findByAuthority(RoleType role) {
        return roleRepository.findByAuthority(role.value())
                .orElseThrow(() -> new RoleNotFoundException(
                        "Failed to find role: Role not found with authority " + role.value()));
    }

    public boolean existsByAuthority(String authority) {
        return roleRepository.existsByAuthority(authority);
    }
}