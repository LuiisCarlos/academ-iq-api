package dev.luiiscarlos.academ_iq_api;

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

    public static void main(String[] args) {
        loadEnvToSystemProperties();

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

    private static void loadEnvToSystemProperties() {
        String activeProfile = System.getProperty("spring.profiles.active");
        Dotenv dotenv = null;
        String dotenvFilename;

        if ("dev".equalsIgnoreCase(activeProfile))
            dotenvFilename = ".env.dev";
        else if ("prod".equalsIgnoreCase(activeProfile))
            dotenvFilename = ".env.prod";
        else
            dotenvFilename = ".env";

        try {
            dotenv = Dotenv.configure()
                    .filename(dotenvFilename)
                    .ignoreIfMissing()
                    .load();
            dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        } catch (DotenvException ignored) { }

        if ((dotenv == null || dotenv.entries().isEmpty()) && !".env".equals(dotenvFilename)) {
            try {
                dotenv = Dotenv.configure()
                        .filename(".env")
                        .ignoreIfMissing()
                        .load();
                dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
            } catch (DotenvException ignored) { }
        }
    }
}
