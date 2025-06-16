package dev.luiiscarlos.academ_iq_api.core.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import dev.luiiscarlos.academ_iq_api.core.filters.GlobalExceptionFilter;

@EnableWebMvc
@Configuration
public class SecurityConfig {

    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Configures the security filter chain for the application
     *
     * @param http                    the HttpSecurity object to configure
     * @param globalExceptionFilter   the filter for handling exceptions
     * @param jwtAuthenticationFilter the filter for handling access tokens
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            GlobalExceptionFilter globalExceptionFilter,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            Converter<Jwt, AbstractAuthenticationToken> customJwtAuthenticationConverter) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/actuator/**").hasAnyRole("ADMIN", "ENDPOINT_ADMIN")
                        .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/courses/{id}/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/courses/**").permitAll()
                        .requestMatchers("/v1/courses/**").hasAnyRole("ADMIN", "ACADEMIQ_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/enrollments/@me/**").authenticated()
                        .requestMatchers("/v1/users/@me/**").authenticated()
                        .requestMatchers("/v1/users/**").hasAnyRole("ADMIN", "ACADEMIQ_ADMIN")
                        .requestMatchers("/v1/files/**").permitAll()
                        .anyRequest().authenticated());

        http.oauth2ResourceServer(server -> server
                .jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler));

        http.addFilterBefore(globalExceptionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates a PasswordEncoder bean that uses BCrypt for password encoding
     *
     * @return a PasswordEncoder instance
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a CorsConfigurationSource bean that configures CORS settings
     *
     * @return a CorsConfigurationSource instance
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
