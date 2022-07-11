package ru.alexander.convertio.conversions.source.rapidapi;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.function.Supplier;

public class RetryHelper {
    public static <T> T runWithRetry(RetryTemplate retry, Supplier<T> action) {
        try {
            return retry.execute((RetryCallback<T, Throwable>) context -> action.get());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private RetryHelper() {
    }
}
