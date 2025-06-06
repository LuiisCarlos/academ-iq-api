package dev.luiiscarlos.academ_iq_api.services;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.auth.RoleNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.repositories.RoleRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByAuthority(String authority) {
        return roleRepository.findByAuthority(authority)
            .orElseThrow(() -> new RoleNotFoundException("Failed to find role: Role not found with authority " + authority));
    }
}