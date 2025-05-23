package dev.luiiscarlos.academ_iq_api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UtilConfiguration {

    // private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy' 'HH:mm:ss");

    @Bean
    Dotenv dotenv() {
        Dotenv dotenv;

        dotenv = Dotenv.configure().ignoreIfMissing() .load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        return dotenv;
    }

    /* @Bean
    ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    } */

    @Bean
    Cloudinary cloudinary(Dotenv dotenv) {
        Map<String, Object> config = new HashMap<>();

        config.put("cloud_name", dotenv.get("CLOUD_API_NAME"));
        config.put("api_key", dotenv.get("CLOUD_API_KEY"));
        config.put("api_secret", dotenv.get("CLOUD_API_SECRET"));
        config.put("secure", true);             // HTTPS
        config.put("connect_timeout", 30000);   // 30 segundos

        return new Cloudinary(config);
    }

}
