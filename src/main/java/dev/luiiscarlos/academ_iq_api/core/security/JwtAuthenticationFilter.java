package dev.luiiscarlos.academ_iq_api.core.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorHandler;
import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.auth.security.InvalidTokenTypeException;
import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.user.service.impl.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserDetailsServiceImpl userDetailsService;

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
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer")) {
            String endpoint = request.getRequestURI();
            tokenService.validate(token, null);

            Jwt jwt = tokenService.decode(token);
            Instant expiresAt = jwt.getExpiresAt();
            String tokenType = jwt.getClaimAsString("token_type");
            String subject = jwt.getSubject();

            System.out.println(subject);

            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                ErrorHandler.createErrorResponse(response, HttpStatus.UNAUTHORIZED, ErrorMessages.EXPIRED_TOKEN);
                return;
            }

            if (isRefreshPath(endpoint)) {
                if (!"refresh".equals(tokenType))
                    throw new InvalidTokenTypeException(ErrorMessages.INVALID_TOKEN_TYPE);
            } else {
                if (!"access".equals(tokenType))
                    throw new InvalidTokenTypeException(ErrorMessages.INVALID_TOKEN_TYPE);
            }

            User currentUser = (User) userDetailsService.loadUserByUsername(subject);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    currentUser, null, currentUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
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
        return path.contains("/auth/refresh") || path.contains("/auth/logout") || path.contains("/auth/verify");
    }

}
