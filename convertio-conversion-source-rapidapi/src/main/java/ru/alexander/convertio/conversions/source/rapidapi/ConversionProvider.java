package ru.alexander.convertio.conversions.source.rapidapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.conversions.source.api.ConversionSource;
import ru.alexander.convertio.model.Money;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.GET;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.API_KEY;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.HOST;
import static ru.alexander.convertio.conversions.source.rapidapi.RetryHelper.runWithRetry;

@Service
@RequiredArgsConstructor
class ConversionProvider implements ConversionSource {
    private static final String MSG_CONVERSION_HTTP_ERROR = "Conversion failed. " +
        "We've received code %s from conversion web service";

    private final RestTemplate http;
    private final ObjectMapper mapper;
    private final RetryTemplate retry;

    @Override
    public Money convert(Money from, String targetCurrency) {
        return runWithRetry(retry, () -> tryToConvert(from, targetCurrency));
    }

    private Money tryToConvert(Money from, String targetCurrency) {
        val url = conversionUrl(from, targetCurrency);
        val response = http.exchange(url, GET, getRequest(), String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            val msg = String.format(MSG_CONVERSION_HTTP_ERROR, response.getStatusCodeValue());
            throw new HttpClientErrorException(response.getStatusCode(), msg);
        }
        Money target;
        val json = ofNullable(response.getBody())
            .map(content -> {
                try {
                    return mapper.readTree(content);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .orElseThrow(() -> new RuntimeException("Failed to retrieve conversion"));
        val rates = json.get("rates");
        target = Money.builder()
            .currency(rates.fieldNames().next())
            .amount(rates.get(targetCurrency.toUpperCase()).get("rate_for_amount").asDouble())
            .build();
        return target;
    }

    private static HttpEntity<Void> getRequest() {
        val headers = new HttpHeaders();
        headers.set("X-Rapidapi-Key", API_KEY);
        headers.set("X-Rapidapi-Host", HOST);
        headers.set("Host", HOST);
        return new HttpEntity<>(null, headers);
    }

    private static String conversionUrl(Money from, String toCurrency) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("currency-converter5.p.rapidapi.com")
            .path("/currency/convert")
            .queryParam("format", "json")
            .queryParam("from", from.getCurrency())
            .queryParam("to", toCurrency)
            .queryParam("amount", from.getAmount())
            .build()
            .toUriString();
    }
}
