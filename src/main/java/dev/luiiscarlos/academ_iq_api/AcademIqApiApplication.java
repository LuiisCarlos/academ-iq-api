package dev.luiiscarlos.academ_iq_api;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.FileServiceImpl;
import dev.luiiscarlos.academ_iq_api.services.RoleService;
import dev.luiiscarlos.academ_iq_api.services.interfaces.TokenService;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "dev.luiiscarlos.academ_iq_api.repositories")
@EntityScan(basePackages = "dev.luiiscarlos.academ_iq_api.models")
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
			RoleService roleService,
			FileServiceImpl fileService) {
		return args -> {
			File avatar = fileService.findByFilename("default-user-avatar.png");

			refreshTokenRepository.findByUser(userRepository.findByUsername("admin").orElse(null)).ifPresent(token -> { refreshTokenRepository.delete(token); });
			refreshTokenRepository.findByUser(userRepository.findByUsername("user").orElse(null)).ifPresent(token -> { refreshTokenRepository.delete(token); });
			userRepository.findByUsername("admin").ifPresent(user -> { userRepository.delete(user); });
			userRepository.findByUsername("user").ifPresent(user -> { userRepository.delete(user); });

			Role userRole = roleService.findByAuthority("USER");
			Role adminRole = roleService.findByAuthority("ADMIN");

			User user = User.builder()
				.username("user")
				.password("{bcrypt}" + passwordEncoder.encode("user"))
				.email("user@academ-iq.net")
				.firstname("user")
				.lastname("user")
				.birthdate(LocalDate.of(2000,1,1))
				.authorities(Set.of(userRole))
				.avatar(avatar)
				.phone("999999999")
				.isVerified(true)
				.build();
			User admin = User.builder()
				.username("admin")
				.password("{bcrypt}" + passwordEncoder.encode("admin"))
				.email("admin@academ-iq.net")
				.firstname("admin")
				.lastname("admin")
				.birthdate(LocalDate.of(2000,1,1))
				.authorities(Set.of(adminRole))
				.avatar(avatar)
				.phone("999999999")
				.isVerified(true)
				.build();

			userRepository.save(user);
			userRepository.save(admin);

			System.out.println("\nUser access token: " + tokenService.generateAccessToken(user) + "\n");
			refreshTokenRepository.save(tokenService.generateRefreshToken(user));

			System.out.println("Admin access token: " + tokenService.generateAccessToken(admin) + "\n");
			refreshTokenRepository.save(tokenService.generateRefreshToken(admin));
		};
	}

}
