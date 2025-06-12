package dev.luiiscarlos.academ_iq_api.core.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorHandler;
import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.auth.security.InvalidTokenTypeException;
import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    /**
     * Filters the request and checks if the access token is valid and if it is,
     * it sets the authentication
     *
     * @param request     The request to filter
     * @param response    The response to send the error to
     * @param filterChain The filter chain to continue the filter
     * @throws ServletException If a servlet error occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        String endpoint = request.getRequestURI();

        if (token != null && token.startsWith("Bearer")) {
            tokenService.validate(token, null);

            Jwt jwt = tokenService.decode(token);
            Instant expiresAt = jwt.getExpiresAt();
            String tokenType = jwt.getClaimAsString("token_type");

            if (expiresAt.isBefore(Instant.now())) {
                ErrorHandler.setCustomErrorResponse(response, HttpStatus.UNAUTHORIZED,
                        ErrorMessages.EXPIRED_TOKEN);
                return;
            }

            if (isRefreshPath(endpoint)) {
                if (!"refresh".equals(tokenType))
                    throw new InvalidTokenTypeException(ErrorMessages.INVALID_TOKEN_TYPE);
            } else {
                if (!"access".equals(tokenType)) {
                    ErrorHandler.setCustomErrorResponse(response, HttpStatus.UNAUTHORIZED,
                            ErrorMessages.INVALID_TOKEN_TYPE);
                    return;
                }
            }

            Authentication authentication = new JwtAuthenticationToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the path is a refresh path
     *
     * @param path the path to check
     * @return true if the path is a refresh path, false otherwise
     */
    private boolean isRefreshPath(String path) {
        return path.contains("/auth/refresh") ||
                path.contains("/auth/logout") ||
                path.contains("/auth/verify");
    }

}
