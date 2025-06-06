package dev.luiiscarlos.academ_iq_api.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class UtilityConfiguration {

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

    /**
     * Creates and configure a Cloudinary bean to upload files to the cloud
     *
     * @param dotenv the environment variables needed for configuration
     * @return a cloudinary instance
     */
    @Bean
    Cloudinary cloudinary(Dotenv dotenv) {
        Map<String, Object> config = new HashMap<>();

        config.put("cloud_name", dotenv.get("CLOUDINARY_NAME"));
        config.put("api_key", dotenv.get("CLOUDINARY_KEY"));
        config.put("api_secret", dotenv.get("CLOUDINARY_SECRET"));
        config.put("secure", true);
        config.put("connect_timeout", 30000);

        return new Cloudinary(config);
    }

}
