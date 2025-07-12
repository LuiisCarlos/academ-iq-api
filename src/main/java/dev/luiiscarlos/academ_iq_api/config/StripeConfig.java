package dev.luiiscarlos.academ_iq_api.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class StripeConfig {

    @Bean
    boolean init(Dotenv dotenv) {
        String stripeApiKey = dotenv.get("STRIPE_SECRET");

        if (Objects.isNull(stripeApiKey) || stripeApiKey.isEmpty()) // TODO Change exception
            throw new IllegalArgumentException("Stripe API key is not set in environment variables");

        return true;
    }

}
