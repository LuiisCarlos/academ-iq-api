package dev.luiiscarlos.academ_iq_api.config.filters;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.luiiscarlos.academ_iq_api.exceptions.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    private final ObjectMapper objectMapper;

    /**
     * Filters the request and checks if the access token is valid and if it is, it sets the authentication
     *
     * @param request The request to filter
     * @param response The response to send the error to
     * @param filterChain The filter chain to continue the filter
     * @throws ServletException If a servlet error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            Jwt jwt = jwtDecoder.decode(token);

            Instant expiresAt = jwt.getExpiresAt();

            if (expiresAt == null || expiresAt.isBefore(Instant.now())){
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token is expired");
                return;
            }

            if (!"access".equals(jwt.getClaim("token_type"))) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token is not an access token");
                return;
            }

            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Sends an error response to the client
     *
     * @param response The response to send the error to
     * @param status The status of the error
     * @param message The message of the error
     * @throws IOException If an I/O error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}
