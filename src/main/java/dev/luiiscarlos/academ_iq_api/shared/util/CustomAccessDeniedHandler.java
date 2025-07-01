package dev.luiiscarlos.academ_iq_api.shared.util;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorHandler;
import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorMessages;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles access denied exceptions by sending a 403 Forbidden response with an error message.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param accessDeniedException the AccessDeniedException that was thrown
     * @throws IOException if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorHandler.createErrorResponse(response, HttpStatus.FORBIDDEN, ErrorMessages.ACCESS_DENIED);
    }

}