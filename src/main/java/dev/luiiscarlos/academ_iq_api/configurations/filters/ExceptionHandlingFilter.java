package dev.luiiscarlos.academ_iq_api.configurations.filters;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.luiiscarlos.academ_iq_api.utilities.ErrorHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private final ErrorHandler errorHandler;

    /**
     * Filters the request and sends an error response if an exception is thrown
     *
     * @param request     The request to filter
     * @param response    The response to send the error to
     * @param filterChain The filter chain to continue
     *
     * @throws ServletException If a servlet exception occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            errorHandler.setCustomErrorResponse(response, HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

}
