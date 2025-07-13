package dev.luiiscarlos.academ_iq_api.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class StripeConfig {

    @Bean
    boolean init(Environment env) {
        String stripeSecret = env.getProperty("stripe.secret");

        if (Objects.isNull(stripeSecret) || stripeSecret.isEmpty()) // TODO Change exception
            throw new IllegalArgumentException("Stripe API key is not set in environment variables");

        return true;
    }

}
