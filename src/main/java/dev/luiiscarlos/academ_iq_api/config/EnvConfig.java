package dev.luiiscarlos.academ_iq_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        return dotenv;
    }

}