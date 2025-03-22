package dev.luiiscarlos.academ_iq_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfiguration {

    private final Dotenv dotenv;

    public EnvConfiguration() {
        this.dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    }

    @Bean
    Dotenv dotenv() {
        return dotenv;
    }

    @PostConstruct
    public void init() {
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }

}
