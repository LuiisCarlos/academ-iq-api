package dev.luiiscarlos.academ_iq_api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    /**
     * Creates and configure a Cloudinary bean to upload files
     *
     * @param dotenv the environment variables needed for configuration
     * @return a cloudinary instance
     */
    @Bean
    Cloudinary cloudinary(Environment env) {
        Map<String, Object> config = new HashMap<>();

        config.put("cloud_name", env.getProperty("storage.cloudinary.name"));
        config.put("api_key", env.getProperty("storage.cloudinary.key"));
        config.put("api_secret", env.getProperty("storage.cloudinary.secret"));
        config.put("secure", true);
        config.put("connect_timeout", 30000);

        return new Cloudinary(config);
    }

}
