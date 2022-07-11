package ru.alexander.convertio.conversions.source.rapidapi;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.conversions.source.api.ConversionSource;
import ru.alexander.convertio.model.Money;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.GET;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiHelper.getRequest;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiHelper.runWithRetry;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.RAPID_API_HOST;

@Service
@RequiredArgsConstructor
class ConversionProvider implements ConversionSource {
    private static final String MSG_CONVERSION_HTTP_ERROR = "Conversion failed. " +
        "We've received code %s from conversion web service";

    private final RestTemplate http;
    private final RetryTemplate retry;

    @Override
    public Money convert(Money from, String targetCurrency) {
        return runWithRetry(retry, () -> tryToConvert(from, targetCurrency));
    }

    private Money tryToConvert(Money from, String targetCurrency) {
        val url = conversionUrl(from, targetCurrency);
        val response = http.exchange(url, GET, getRequest(), JsonNode.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            val msg = String.format(MSG_CONVERSION_HTTP_ERROR, response.getStatusCodeValue());
            throw new HttpClientErrorException(response.getStatusCode(), msg);
        }
        return ofNullable(response.getBody())
            .map((json) -> json.get("rates"))
            .map((rates) -> Money.builder()
                .currency(rates.fieldNames().next())
                .amount(rates.get(targetCurrency.toUpperCase()).get("rate_for_amount").asDouble())
                .build())
            .orElseThrow(() -> new RuntimeException("Failed to retrieve conversion"));
    }

    private static String conversionUrl(Money from, String toCurrency) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(RAPID_API_HOST)
            .path("/currency/convert")
            .queryParam("format", "json")
            .queryParam("from", from.getCurrency())
            .queryParam("to", toCurrency)
            .queryParam("amount", from.getAmount())
            .build()
            .toUriString();
    }
}
