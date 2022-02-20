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
import ru.alexander.convertio.conversions.source.api.CurrenciesSource;
import ru.alexander.convertio.http.client.HttpClientConfiguration;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
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
import static ru.alexander.convertio.test.helper.TestHelper.randomCurrencies;
import static ru.alexander.convertio.test.helper.TestHelper.randomString;

@SpringBootTest(classes = {CurrenciesProvider.class})
@Import(HttpClientConfiguration.class)
class CurrenciesProviderTest {
    @Autowired
    private CurrenciesSource service;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private ApiKeyVault apiKeyVault;

    private MockRestServiceServer http;

    @BeforeEach
    void init() {
        http = createServer(restTemplate);
    }

    @Test
    @DisplayName("Context loads")
    void testContext() {
        asList(service, restTemplate, apiKeyVault, http)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Basic currency retrieval works fine")
    void basicCheckWorks() throws Exception {
        val apiKey = randomString();
        when(apiKeyVault.getApiKey())
            .thenReturn(apiKey);
        val expected = randomCurrencies();
        http.expect(
                once(), requestTo(supportedCurrenciesUrl(apiKey))
            )
            .andExpect(method(GET))
            .andRespond(
                withStatus(OK)
                    .contentType(APPLICATION_JSON)
                    .body(currenciesResponse(expected))
            );
        val actual = service.supportedCurrencies();
        http.verify();
        assertEquals(expected, actual);
    }

    private String supportedCurrenciesUrl(String apiKey) {
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("api.exchangeratesapi.io")
            .path("/v1/symbols")
            .queryParam("access_key", apiKey)
            .build()
            .toUriString();
    }

    private static String currenciesResponse(Collection<String> currencies) throws JSONException {
        val symbols = currencies.stream()
            .collect(toMap(identity(), currency -> "Description of " + currency));
        val json = new JSONObject()
            .put("success", true)
            .put("symbols", new JSONObject(symbols));
        return json.toString();
    }

}