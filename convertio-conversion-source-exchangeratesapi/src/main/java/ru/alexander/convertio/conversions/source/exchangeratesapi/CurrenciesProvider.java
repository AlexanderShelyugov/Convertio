package ru.alexander.convertio.conversions.source.exchangeratesapi;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.conversions.source.api.CurrenciesSource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
class CurrenciesProvider implements CurrenciesSource {
    private static final String MSG_CURRENCY_CHECK_HTTP_ERROR = "Currency check failed. " +
        "We've received code %s from conversion web service";

    private final RestTemplate http;
    private final ApiKeyVault apiKey;

    @Override
    public Collection<String> supportedCurrencies() {
        val response = http.getForEntity(supportedCurrenciesUrl(), JsonNode.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            val msg = String.format(MSG_CURRENCY_CHECK_HTTP_ERROR, response.getStatusCodeValue());
            throw new RuntimeException(msg);
        }

        val supportedCurrencies = ofNullable(response.getBody())
            .map(node -> node.get("symbols"))
            .map(JsonNode::fieldNames)
            .map(currencies -> {
                val list = new LinkedList<String>();
                currencies.forEachRemaining(list::add);
                return (Collection<String>) new HashSet<>(list);
            })
            .orElseGet(Collections::emptySet);
        return supportedCurrencies;
    }

    private String supportedCurrenciesUrl() {
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("api.exchangeratesapi.io")
            .path("/v1/symbols")
            .queryParam("access_key", apiKey.getApiKey())
            .build()
            .toUriString();
    }
}
