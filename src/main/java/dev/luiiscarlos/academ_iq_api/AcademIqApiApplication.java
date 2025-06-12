package dev.luiiscarlos.academ_iq_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import dev.luiiscarlos.academ_iq_api.auth.security.RefreshToken;
import dev.luiiscarlos.academ_iq_api.auth.security.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.user.model.User;
import dev.luiiscarlos.academ_iq_api.user.repository.UserRepository;

@SpringBootApplication
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

			System.out.println("\nAccess token: " + tokenService.generateAccessToken(admin));

			RefreshToken refreshToken = refreshTokenRepository.save(tokenService.generateRefreshToken(admin));
			System.out.println("\nRefresh token: " + refreshToken.getToken() + "\n");
		};
	}

}
