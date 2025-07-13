package dev.luiiscarlos.academ_iq_api.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StripeConfig {

    /**
     * Configures Stripe by validating the presence of the Stripe secret key in the environment variables
     *
     * @param env the {@link Environment} used to access application properties
     * @return true if the Stripe secret key is set correctly
     * @throws RuntimeException if the Stripe secret key is missing or empty
     */
    @Bean
    boolean stripe(Environment env) {
        log.debug("Initializing Stripe context");

        String stripeSecret = env.getProperty("stripe.secret");

        if (Objects.isNull(stripeSecret) || stripeSecret.isEmpty())
            throw new RuntimeException("Stripe API key is not set in environment variables");

        return true;
    }

}
