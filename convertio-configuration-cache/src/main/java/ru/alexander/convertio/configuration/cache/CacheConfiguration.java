package ru.alexander.convertio.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.HOURS;

@Configuration
@EnableCaching
public class CacheConfiguration {
    @Bean
    Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(2, HOURS);
    }

    @Bean
    CacheManager cacheManager(Caffeine caffeine) {
        val caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
