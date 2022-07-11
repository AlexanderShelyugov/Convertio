package ru.alexander.convertio.conversions.source.rapidapi;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.conversions.source.api.CurrenciesSource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.GET;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiHelper.getRequest;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiHelper.runWithRetry;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.RAPID_API_HOST;

@Service
@RequiredArgsConstructor
class CurrenciesProvider implements CurrenciesSource {

    private final RestTemplate http;
    private final RetryTemplate retryTemplate;

    @Override
    public Collection<String> supportedCurrencies() {
        return currenciesWithDescription().keySet();
    }

    @Override
    @Cacheable("currenciesWithDescription")
    public Map<String, String> currenciesWithDescription() {
        return runWithRetry(retryTemplate, this::tryToGetCurrencies);
    }

    private Map<String, String> tryToGetCurrencies() {
        val response = http.exchange(getListUrl(), GET, getRequest(), JsonNode.class);
        val supportedCurrencies = ofNullable(response.getBody())
            .map(node -> node.get("currencies"))
            .map(node -> {
                val currencies = new HashMap<String, String>();
                node.fields().forEachRemaining(
                    field -> currencies.put(field.getKey(), field.getValue().textValue())
                );
                return (Map<String, String>) currencies;
            })
            .orElseGet(Collections::emptyMap);
        return unmodifiableMap(supportedCurrencies);
    }

    private static String getListUrl() {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(RAPID_API_HOST)
            .path("/currency/list")
            .build()
            .toUriString();
    }
}
