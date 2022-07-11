package ru.alexander.convertio.conversions.source.rapidapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@EnableRetry
class RapidApiConfiguration {
    @Bean
    RetryTemplate rapidAPIRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(SECONDS.toMillis(2));
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(12);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
