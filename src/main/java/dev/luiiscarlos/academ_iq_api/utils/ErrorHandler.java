package dev.luiiscarlos.academ_iq_api.utils;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.luiiscarlos.academ_iq_api.exceptions.handlers.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErrorHandler {

    private final ObjectMapper objectMapper;

    public void setCustomErrorResponse(HttpServletResponse response, HttpStatus status, String message)
    throws IOException {
        response.setStatus(status.value());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .statusCode(status.value())
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.flushBuffer();
    }

}
