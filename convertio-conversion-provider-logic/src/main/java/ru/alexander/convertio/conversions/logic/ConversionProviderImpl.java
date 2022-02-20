package ru.alexander.convertio.conversions.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.conversions.api.ConversionFailedException;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
class ConversionProviderImpl implements ConversionProvider {
    private static final String MSG_CONVERSION_HTTP_ERROR = "Conversion failed. " +
        "We've received code %s from conversion web service";

    private final ApiKeyVault apiKeyVault;
    private final RestTemplate http;
    private final ObjectMapper mapper;

    @Override
    public MoneyConversion convert(Money source, String targetCurrency)
        throws ConversionFailedException {
        val url = conversionUrl(apiKeyVault.getApiKey(), source, targetCurrency);
        val response = http.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            val msg = String.format(MSG_CONVERSION_HTTP_ERROR, response.getStatusCodeValue());
            throw new ConversionFailedException(msg);
        }
        Money target;
        try {
            val json = ofNullable(response.getBody())
                .map(content -> {
                    try {
                        return mapper.readTree(content);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new Exception("Failed to retrieve conversion"));
            target = Money.builder()
                .currency(json.get("query").get("to").textValue())
                .amount(json.get("result").doubleValue())
                .build();
        } catch (Exception e) {
            throw new ConversionFailedException(e);
        }
        return MoneyConversion.builder()
            .from(source)
            .to(target)
            .build();
    }

    private static String conversionUrl(String key, Money from, String toCurrency) {
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("api.exchangeratesapi.io")
            .path("/v1/convert")
            .queryParam("access_key", key)
            .queryParam("from", from.getCurrency())
            .queryParam("to", toCurrency)
            .queryParam("amount", from.getAmount())
            .build()
            .toUriString();
    }
}
