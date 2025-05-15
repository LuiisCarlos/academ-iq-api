package dev.luiiscarlos.academ_iq_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import dev.luiiscarlos.academ_iq_api.config.filters.AccessTokenFilter;
import dev.luiiscarlos.academ_iq_api.config.filters.ExceptionHandlingFilter;
import dev.luiiscarlos.academ_iq_api.utils.ErrorHandler;
import lombok.RequiredArgsConstructor;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccessTokenFilter accessTokenFilter;

    private final ExceptionHandlingFilter exceptionHandlingFilter;

    private final ErrorHandler errorHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AccessDeniedHandler accessDeniedHandler)
            throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/actuator/**").hasAnyRole("ADMIN", "ENDPOINT_ADMIN")
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/courses/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/courses/{id}/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/courses/**").permitAll()
                .requestMatchers("/api/v1/courses/**").hasAnyRole("ADMIN", "ACADEMIQ_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/users/enrollments/@me/**").authenticated()
                .requestMatchers("/api/v1/users/@me/**").authenticated()
                .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "ACADEMIQ_ADMIN")
                .requestMatchers("/api/v1/files/**").permitAll()
                .anyRequest().authenticated());

        http
            .oauth2ResourceServer(server -> server
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler));

        http
            .addFilterBefore(exceptionHandlingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            errorHandler.setCustomErrorResponse(response, HttpStatus.FORBIDDEN,
                    "Failed to access resource: Access denied");
        };
    }

}
