package dev.luiiscarlos.academ_iq_api.features.user.security;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.auth.exception.RoleNotFoundException;
import dev.luiiscarlos.academ_iq_api.shared.enums.RoleType;

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
        return roleRepository.findByAuthority(role.name())
                .orElseThrow(() -> new RoleNotFoundException(
                        "Failed to find role: Role not found with authority " + role.name()));
    }

    public boolean existsByAuthority(String authority) {
        return roleRepository.existsByAuthority(authority);
    }
}