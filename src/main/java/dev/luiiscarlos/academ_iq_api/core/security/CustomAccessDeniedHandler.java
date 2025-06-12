package dev.luiiscarlos.academ_iq_api.core.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorHandler;
import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Creates an AccessDeniedHandler bean that handles access denied exceptions
     *
     * @param errorHandler the error handler to use for setting the error response
     * @return an AccessDeniedHandler instance
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorHandler.setCustomErrorResponse(response, HttpStatus.FORBIDDEN, ErrorMessages.ACCESS_DENIED);
    }

}