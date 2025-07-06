package dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserCrudService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCrudService userQueryService;

    @Override
    public UserDetails loadUserByUsername(String username){
        return userQueryService.findByUsername(username);
    }

}
