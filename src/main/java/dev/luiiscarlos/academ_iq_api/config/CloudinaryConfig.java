package dev.luiiscarlos.academ_iq_api.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    /**
     * Creates and configures a Cloudinary bean for uploading files
     *
     * @param env the {@link Environment} used to access Cloudinary configuration properties
     * @return a configured {@link Cloudinary} instance
     * @throws RuntimeException if any required environment variable is missing or empty
     */
    @Bean
    Cloudinary cloudinary(Environment env) {
        String cloudName = env.getProperty("cloudinary.name");
        String apiKey = env.getProperty("cloudinary.key");
        String apiSecret = env.getProperty("cloudinary.secret");

        if ((Objects.isNull(cloudName) || cloudName.isEmpty()) ||
                (Objects.isNull(apiKey) || apiKey.isEmpty()) ||
                (Objects.isNull(apiSecret) || apiSecret.isEmpty()))
            throw new RuntimeException("Failed to load environments variables required for Cloudinary");

        Map<String, Object> config = new HashMap<>();

        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", true);
        config.put("connect_timeout", 20000);

        return new Cloudinary(config);
    }

}
