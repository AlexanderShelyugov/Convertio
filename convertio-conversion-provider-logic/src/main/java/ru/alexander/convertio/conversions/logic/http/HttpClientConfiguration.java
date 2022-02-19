package ru.alexander.convertio.conversions.logic.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class HttpClientConfiguration {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
