package dev.luiiscarlos.academ_iq_api.config.filters;

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

import dev.luiiscarlos.academ_iq_api.services.TokenServiceImpl;
import dev.luiiscarlos.academ_iq_api.utils.ErrorHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    private final TokenServiceImpl tokenService;

    private final ErrorHandler errorHandler;

    /**
     * Filters the request and checks if the access token is valid and if it is, it
     * sets the authentication
     *
     * @param request     The request to filter
     * @param response    The response to send the error to
     * @param filterChain The filter chain to continue the filter
     *
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
        String path = request.getRequestURI();

        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
            Jwt jwt = tokenService.getJwtToken(token);
            Instant expiresAt = jwt.getExpiresAt();
            String tokenType = jwt.getClaimAsString("token_type");

            if (!tokenService.isValidToken(token)) {
                errorHandler.setCustomErrorResponse(response, HttpStatus.UNAUTHORIZED,
                        "Failed to validate Token: Invalid access token");
                return;
            }

            if (expiresAt.isBefore(Instant.now())) {
                errorHandler.setCustomErrorResponse(response, HttpStatus.UNAUTHORIZED,
                        "Failed to validate Token: Expired access token");
                return;
            }

            if (isRefreshPath(path)) {
                if (!"refresh".equals(tokenType))
                    throw new RuntimeException("Failed to validate Token: Invalid token type"); // TODO: Review this
            } else {
                if (!"access".equals(tokenType)) {
                    errorHandler.setCustomErrorResponse(response, HttpStatus.UNAUTHORIZED,
                            "Failed to validate Token: Invalid token type");
                    return;
                }
            }

            Authentication authentication = new JwtAuthenticationToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRefreshPath(String path) {
        return path.contains("/api/v1/auth/refresh") ||
                path.contains("/api/v1/auth/logout") ||
                path.contains("/api/v1/auth/verify");
    }

}
