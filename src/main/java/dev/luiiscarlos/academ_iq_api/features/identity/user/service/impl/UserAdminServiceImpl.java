package dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.identity.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.features.identity.auth.service.AuthService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.security.Role;
import dev.luiiscarlos.academ_iq_api.features.identity.user.security.RoleService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.security.RoleType;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserAdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

	private final static String BCRYPT_PREFIX = "{bcrypt}";

	private final UserQueryService userQueryService;

	private final UserMapper userMapper;

	private final RoleService roleService;

	private final AuthService authService;

	private final TokenService tokenService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Page<UserResponse> getAll(Pageable pageable) {
		Page<User> users = userQueryService.findAll(pageable);

		return users.map(userMapper::toUserResponse);
	}

	@Override
	public void assignRole(long userId, RoleType role) {
		User user = userQueryService.findById(userId);
		Role authority = roleService.findByAuthority(role);

		Set<Role> authorities = user.getAuthorities().stream()
				.filter(Role.class::isInstance)
				.map(Role.class::cast)
				.collect(Collectors.toSet());

		authorities.add(authority);
		user.setAuthorities(authorities);

		userQueryService.save(user);
	}

	@Override
	public void removeRole(long userId, RoleType role) {
		User user = userQueryService.findById(userId);
		Role authority = roleService.findByAuthority(role);

		Set<Role> authorities = user.getAuthorities().stream()
				.filter(Role.class::isInstance)
				.map(Role.class::cast)
				.collect(Collectors.toSet());

		authorities.remove(authority);
		user.setAuthorities(authorities);

		userQueryService.save(user);
	}

	@Override
	public void setRoles(long userId, List<RoleType> roles) {
		User user = userQueryService.findById(userId);

		Set<Role> authorities = roles.stream()
				.map(roleService::findByAuthority)
				.collect(Collectors.toSet());

		user.setAuthorities(authorities);

		userQueryService.save(user);
	}

	@Override
	public void changePassword(long userId, String newPassword) {
		User user = userQueryService.findById(userId);

		user.setPassword(BCRYPT_PREFIX + passwordEncoder.encode(newPassword));

		userQueryService.save(user);
	}

	@Override
	public void forceLogout(long userId) {
		String refreshToken = tokenService.findByUserId(userId).getToken();

		authService.logout(userId, refreshToken);
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
