package ru.alexander.convertio.conversions.logic;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.conversions.api.CurrenciesService;

@Service
@RequiredArgsConstructor
class CurrenciesServiceImpl implements CurrenciesService {
    private final RestTemplate http;
    private final ApiKeyVault apiKey;

    @Override
    public boolean isCurrencySupported(String currency) {
        val response = http.getForEntity(supportedCurrenciesUrl(), JsonNode.class);
        return true;
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
