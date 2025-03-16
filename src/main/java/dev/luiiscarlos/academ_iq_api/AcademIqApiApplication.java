package dev.luiiscarlos.academ_iq_api;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.FileServiceImpl;
import dev.luiiscarlos.academ_iq_api.services.RoleService;
import dev.luiiscarlos.academ_iq_api.services.interfaces.TokenService;

@SpringBootApplication
public class AcademIqApiApplication {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AcademIqApiApplication.class, args);

		FileServiceImpl fileService = context.getBean(FileServiceImpl.class);
		fileService.init();
	}

	@Bean
	@Profile("dev")
	CommandLineRunner run(UserRepository userRepository,
			RefreshTokenRepository refreshTokenRepository,
			TokenService tokenService,
			PasswordEncoder passwordEncoder,
			RoleService roleService) {
		return args -> {
			Role adminRole = roleService.findByAuthority("ADMIN");
        	Set<Role> authorities = Set.of(adminRole);

			User user = User.builder()
				.username("admin")
				.password("{bcrypt}" + passwordEncoder.encode("admin"))
				.email("admin@academ-iq.net")
				.firstname("admin")
				.lastname("admin")
				.birthdate(LocalDate.of(2000,1,1))
				.authorities(authorities)
				.phone("999999999")
				.isAccountVerified(true)
				.build();
			userRepository.save(user);

			System.out.println("\nAdmin access token: " + tokenService.generateAccessToken(user) + "\n");
			refreshTokenRepository.save(tokenService.generateRefreshToken(user));
		};
	}

}
