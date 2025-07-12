package dev.luiiscarlos.academ_iq_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.luiiscarlos.academ_iq_api.AcademIqApiApplication;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EnvConfig {

    /**
     * Creates a Dotenv bean to obtain the environment variables
     *
     * @return a Dotenv instance
     */
    @Bean
    Dotenv dotenv() {
        return AcademIqApiApplication.DOTENV;
    }

}