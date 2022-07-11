package ru.alexander.convertio.conversions.source.rapidapi;

import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.function.Supplier;

import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.*;

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
        headers.set(API_KEY_HEADER, API_KEY);
        headers.set(RAPID_API_HOST_HEADER, RAPID_API_HOST);
        headers.set(HOST_HEADER, RAPID_API_HOST);
        return new HttpEntity<>(null, headers);
    }

    private RapidApiHelper() {
    }
}
