package dev.luiiscarlos.academ_iq_api.core.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import dev.luiiscarlos.academ_iq_api.core.utils.RSAKeyProperties;

@Configuration
public class JwtConfig {

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
}
