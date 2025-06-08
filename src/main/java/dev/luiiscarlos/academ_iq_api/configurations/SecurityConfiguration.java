package dev.luiiscarlos.academ_iq_api.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import dev.luiiscarlos.academ_iq_api.configurations.filters.AccessTokenFilter;
import dev.luiiscarlos.academ_iq_api.configurations.filters.ExceptionFilter;
import dev.luiiscarlos.academ_iq_api.exceptions.ErrorHandler;
import dev.luiiscarlos.academ_iq_api.exceptions.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.utilities.RSAKeyProperties;

@EnableWebMvc
@Configuration
public class SecurityConfiguration {

    /**
     * Configures the security filter chain for the application
     *
     * @param http                the HttpSecurity object to configure
     * @param accessDeniedHandler the handler for access denied exceptions
     * @param exceptionFilter     the filter for handling exceptions
     * @param accessTokenFilter   the filter for handling access tokens
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AccessDeniedHandler accessDeniedHandler,
            ExceptionFilter exceptionFilter,
            AccessTokenFilter accessTokenFilter)
            throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/actuator/**").hasAnyRole("ADMIN", "ENDPOINT_ADMIN")
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
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler));

        http.addFilterBefore(exceptionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates a JwtDecoder bean that decodes JWT tokens using the provided RSA keys
     *
     * @param RSAKeys the RSA key properties containing the public key
     * @return a JwtDecoder instance
     */
    @Bean
    JwtDecoder jwtDecoder(RSAKeyProperties RSAKeys) {
        return NimbusJwtDecoder.withPublicKey(RSAKeys.getPublicKey()).build();
    }

    /**
     * Creates a JwtEncoder bean that encodes JWT tokens using the provided RSA keys
     *
     * @param RSAKeys the RSA key properties containing the public and private keys
     * @return a JwtEncoder instance
     */
    @Bean
    JwtEncoder jwtEncoder(RSAKeyProperties RSAKeys) {
        JWK jwk = new RSAKey.Builder(RSAKeys.getPublicKey())
                .privateKey(RSAKeys.getPrivateKey())
                .build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
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
     * Creates a JwtAuthenticationConverter bean that converts JWT tokens
     * to authentication objects with roles prefixed by "ROLE_"
     *
     * @return a JwtAuthenticationConverter instance
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
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

    /**
     * Creates an AccessDeniedHandler bean that handles access denied exceptions
     *
     * @param errorHandler the error handler to use for setting the error response
     * @return an AccessDeniedHandler instance
     */
    @Bean
    AccessDeniedHandler accessDeniedHandler(ErrorHandler errorHandler) {
        return (request, response, accessDeniedException) -> {
            errorHandler.setCustomErrorResponse(response, HttpStatus.FORBIDDEN, ErrorMessages.ACCESS_DENIED);
        };
    }

}
