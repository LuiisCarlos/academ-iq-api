package dev.luiiscarlos.academ_iq_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import dev.luiiscarlos.academ_iq_api.config.filters.AccessTokenFilter;
import dev.luiiscarlos.academ_iq_api.config.filters.ExceptionHandlingFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccessTokenFilter accessTokenFilter;

    private final ExceptionHandlingFilter exceptionHandlingFilter;

    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    @SuppressWarnings("removal") // <- For H2 Console
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v1/auth/change-password/**").authenticated()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/courses/{id}/**").authenticated()
                .requestMatchers("/api/v1/courses/**").permitAll()
                .requestMatchers("/api/v1/users/@me/**").authenticated()
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/files/**").authenticated()
                .anyRequest().authenticated());

        http
            .headers(headers -> headers.frameOptions().disable()); // <- For H2 Console

        http
            .oauth2ResourceServer(server -> server
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler));

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

}
