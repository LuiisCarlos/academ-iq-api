package dev.luiiscarlos.academ_iq_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;

import dev.luiiscarlos.academ_iq_api.utils.ErrorHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UtilConfiguration {

    private final ErrorHandler errorHandler;

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            errorHandler.setCustomErrorResponse(response, HttpStatus.FORBIDDEN,
                "Failed to access resource: Access denied");
        };
    }

}
