package ru.alexander.convertio.conversions.source.exchangeratesapi;

import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.alexander.convertio.http.client.HttpClientConfiguration;
import ru.alexander.convertio.model.Money;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.MockRestServiceServer.createServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static ru.alexander.convertio.test.helper.TestHelper.randomAmount;
import static ru.alexander.convertio.test.helper.TestHelper.randomMoney;
import static ru.alexander.convertio.test.helper.TestHelper.randomString;

@SpringBootTest(classes = {ConversionProvider.class})
@Import(HttpClientConfiguration.class)
class ConversionProviderTest {

    @Autowired
    private ConversionProvider provider;

    @MockBean
    private ApiKeyVault apiKeyVault;

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
        asList(provider, apiKeyVault, restTemplate, http)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Successful conversion works")
    void testSuccessfulConversion() throws Exception {
        val apiKey = randomString();
        val from = randomMoney();
        val expected = randomMoney();
        when(apiKeyVault.getApiKey())
            .thenReturn(apiKey);
        http.expect(
                once(),
                requestTo(conversionUrl(apiKey, from, expected))
            )
            .andExpect(method(GET))
            .andRespond(
                withStatus(OK)
                    .contentType(APPLICATION_JSON)
                    .body(conversionResponse(from, expected))
            );
        val actual = provider.convert(from, expected.getCurrency());
        http.verify();
        assertEquals(expected, actual);
    }

    private static String conversionUrl(String key, Money from, Money to) {
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("api.exchangeratesapi.io")
            .path("/v1/convert")
            .queryParam("access_key", key)
            .queryParam("from", from.getCurrency())
            .queryParam("to", to.getCurrency())
            .queryParam("amount", from.getAmount())
            .build()
            .toUriString();
    }

    private static String conversionResponse(Money from, Money to) throws JSONException {
        val response = new JSONObject()
            .put("success", true)
            .put("query", new JSONObject()
                .put("from", from.getCurrency())
                .put("to", to.getCurrency())
                .put("amount", from.getAmount()))
            .put("info", new JSONObject()
                .put("timestamp", now())
                .put("rate", randomAmount()))
            .put("historical", "")
            .put("date", "2022-02-22")
            .put("result", to.getAmount());
        return response.toString();
    }
}