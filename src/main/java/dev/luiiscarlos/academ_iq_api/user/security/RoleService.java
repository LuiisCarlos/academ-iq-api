package dev.luiiscarlos.academ_iq_api.user.security;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.auth.exception.RoleNotFoundException;
import jakarta.transaction.Transactional;

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
}