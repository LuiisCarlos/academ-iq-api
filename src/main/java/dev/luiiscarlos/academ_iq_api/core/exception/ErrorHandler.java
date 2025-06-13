package dev.luiiscarlos.academ_iq_api.core.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ErrorHandler {

    public static void createErrorResponse(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        response.setStatus(status.value());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .statusCode(status.value())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.flushBuffer();
    }

}
