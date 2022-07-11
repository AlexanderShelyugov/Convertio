package ru.alexander.convertio.conversions.source.rapidapi;

import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.http.client.HttpClientConfiguration;
import ru.alexander.convertio.model.Money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.MockRestServiceServer.createServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static ru.alexander.convertio.conversions.source.rapidapi.RapidApiVault.*;
import static ru.alexander.convertio.test.helper.TestHelper.*;

@SpringBootTest(classes = {ConversionProvider.class})
@Import({HttpClientConfiguration.class, RapidApiConfiguration.class})
class ConversionProviderTest {
    @Autowired
    private ConversionProvider provider;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer http;

    @BeforeEach
    void init() {
        http = createServer(restTemplate);
    }

    @Test
    @DisplayName("Context loads")
    void testContext() {
        checkNotNull(provider, restTemplate, http);
    }

    @Test
    @DisplayName("Successful conversion works")
    void testSuccessfulConversion() throws Exception {
        val from = randomMoney();
        val expected = randomMoney();
        http.expect(
                once(),
                requestTo(conversionUrl(from, expected))
            )
            .andExpect(method(GET))
            .andExpect(header(API_KEY_HEADER, API_KEY))
            .andExpect(header(RAPID_API_HOST_HEADER, RAPID_API_HOST))
            .andExpect(header(HOST_HEADER, RAPID_API_HOST))
            .andRespond(
                withStatus(OK)
                    .contentType(APPLICATION_JSON)
                    .body(conversionResponse(from, expected))
            );
        val actual = provider.convert(from, expected.getCurrency());
        http.verify();
        assertEquals(expected, actual);
    }

    private static String conversionUrl(Money from, Money to) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(RAPID_API_HOST)
            .path("/currency/convert")
            .queryParam("format", "json")
            .queryParam("from", from.getCurrency())
            .queryParam("to", to.getCurrency())
            .queryParam("amount", from.getAmount())
            .build()
            .toUriString();
    }

    private static String conversionResponse(Money from, Money to) throws JSONException {
        val response = new JSONObject()
            .put("status", "success")
            .put("amount", from.getAmount())
            .put("base_currency_code", from.getCurrency())
            .put("rates", new JSONObject()
                .put(to.getCurrency().toUpperCase(), new JSONObject()
                    .put("rate", randomAmount())
                    .put("rate_for_amount", to.getAmount())
                )
            )
            .put("updated_date", "2022-02-22");
        return response.toString();
    }
}
