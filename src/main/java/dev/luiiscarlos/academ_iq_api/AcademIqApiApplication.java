package dev.luiiscarlos.academ_iq_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.TokenService;

@SpringBootApplication
@EntityScan(basePackages = "dev.luiiscarlos.academ_iq_api.models")
@EnableJpaRepositories(basePackages = "dev.luiiscarlos.academ_iq_api.repositories")
public class AcademIqApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AcademIqApiApplication.class, args);
	}

	@Bean
	@Profile("dev")
	CommandLineRunner run(
			UserRepository userRepository,
			RefreshTokenRepository refreshTokenRepository,
			TokenService tokenService) {
		return args -> {
			User admin = userRepository.findByUsername("admin").orElse(null);
			User user = userRepository.findByUsername("user").orElse(null);

			System.out.println("\nUser access token: " + tokenService.generateAccessToken(user) + "\n");
			refreshTokenRepository.save(tokenService.generateRefreshToken(user));

			System.out.println("Admin access token: " + tokenService.generateAccessToken(admin) + "\n");
			refreshTokenRepository.save(tokenService.generateRefreshToken(admin));
		};
	}

}
