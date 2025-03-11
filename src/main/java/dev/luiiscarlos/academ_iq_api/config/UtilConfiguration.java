package dev.luiiscarlos.academ_iq_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.luiiscarlos.academ_iq_api.utils.ErrorHandler;

@Configuration
@RequiredArgsConstructor
public class UtilConfiguration {

    private final ErrorHandler errorHandler;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule( new JavaTimeModule() ); // <- For LocalDateTime support
        return objectMapper;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            errorHandler.setCustomErrorResponse(response, HttpStatus.FORBIDDEN,
                "Failed to access resource: Access denied");
        };
    }

}
