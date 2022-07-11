package ru.alexander.convertio.conversions.source.rapidapi;

import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.function.Supplier;

import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.API_KEY;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.HOST;

public class RapidApiHelper {
    static <T> T runWithRetry(RetryTemplate retry, Supplier<T> action) {
        try {
            return retry.execute((RetryCallback<T, Throwable>) context -> action.get());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static HttpEntity<Void> getRequest() {
        val headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", API_KEY);
        headers.set("X-Rapidapi-Host", HOST);
        headers.set("Host", HOST);
        return new HttpEntity<>(null, headers);
    }

    private RapidApiHelper() {
    }
}
