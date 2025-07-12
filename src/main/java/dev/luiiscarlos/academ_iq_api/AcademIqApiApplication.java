package dev.luiiscarlos.academ_iq_api;

import java.io.File;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import dev.luiiscarlos.academ_iq_api.features.identity.auth.security.RefreshToken;
import dev.luiiscarlos.academ_iq_api.features.identity.auth.security.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.features.identity.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.repository.UserRepository;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

@SpringBootApplication
public class AcademIqApiApplication {

    public static Dotenv DOTENV;

    public static void main(String[] args) {
        String profile = Optional.ofNullable(System.getProperty("spring.profiles.active"))
                .orElse("dev");

        loadEnv(profile);

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

    private static void loadEnv(String profile) {
        File envFile = new File(".env." + profile);
        if (!envFile.exists()) {
            envFile = new File(".env");
        }

        try {
            DOTENV = Dotenv.configure()
                    .filename(envFile.getName())
                    .load();

            DOTENV.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch (DotenvException ex) {
            throw new RuntimeException("Failed to load env file: No environment file found.");
        }
    }

}
