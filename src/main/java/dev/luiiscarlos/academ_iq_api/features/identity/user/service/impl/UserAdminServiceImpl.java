package dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.identity.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.features.identity.auth.service.AuthService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.AdminPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserAdminService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserCrudService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.dto.*;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.model.Role;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.service.RoleService;
import dev.luiiscarlos.academ_iq_api.shared.constants.AppDefaults;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {


	private final UserCrudService userQueryService;

	private final UserMapper userMapper;

	private final RoleService roleService;

	private final AuthService authService;

	private final TokenService tokenService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public void setRoles(long userId, RolesRequest request) {
		User user = userQueryService.findById(userId);

		Set<Role> authorities = request.roles().stream()
				.map(roleService::findByAuthority)
				.collect(Collectors.toSet());

		user.setAuthorities(authorities);

		userQueryService.save(user);
	}

	@Override
	public void assignRole(long userId, RoleRequest request) {
		User user = userQueryService.findById(userId);
		Role authority = roleService.findByAuthority(request.role());

		Set<Role> authorities = user.getAuthorities().stream()
				.filter(Role.class::isInstance)
				.map(Role.class::cast)
				.collect(Collectors.toSet());

		authorities.add(authority);
		user.setAuthorities(authorities);

		userQueryService.save(user);
	}

	@Override
	public void removeRole(long userId, RoleRequest request) {
		User user = userQueryService.findById(userId);
		Role authority = roleService.findByAuthority(request.role());

		Set<Role> authorities = user.getAuthorities().stream()
				.filter(Role.class::isInstance)
				.map(Role.class::cast)
				.collect(Collectors.toSet());

		authorities.remove(authority);
		user.setAuthorities(authorities);

		userQueryService.save(user);
	}

	@Override
	public void forceLogout(long userId) {
		String refreshToken = tokenService.findByUserId(userId).getToken();

		authService.logout(userId, refreshToken);
	}

	@Override
	public Page<UserResponse> getAll(Pageable pageable) {
		Page<User> users = userQueryService.findAll(pageable);

		return users.map(userMapper::toDto);
	}

	@Override
	public void changePassword(long userId, AdminPasswordRequest request) {
		User user = userQueryService.findById(userId);

		user.setPassword(AppDefaults.ENCRYPTED_PASSWORD_PREFIX + passwordEncoder.encode(request.password()));

		userQueryService.save(user);
	}

	@Override
	public void activate(long userId) {
		User user = userQueryService.findById(userId);

		user.setEnabled(Boolean.TRUE);

		userQueryService.save(user);
	}

	@Override
	public void deactivate(long userId) {
		User user = userQueryService.findById(userId);

		user.setEnabled(Boolean.FALSE);

		userQueryService.save(user);
	}

}
