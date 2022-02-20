package ru.alexander.convertio.conversions.logic;

import lombok.NonNull;
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
import ru.alexander.convertio.conversions.api.CurrenciesService;
import ru.alexander.convertio.conversions.logic.http.HttpClientConfiguration;
import ru.alexander.convertio.test.helper.TestHelper;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.generate;
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
import static ru.alexander.convertio.test.helper.TestHelper.randomString;

@SpringBootTest(classes = {CurrenciesServiceImpl.class})
@Import(HttpClientConfiguration.class)
class CurrenciesServiceImplTest {
    @Autowired
    private CurrenciesService service;

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
        asList(service, restTemplate, http, apiKeyVault)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Basic check works fine")
    void basicCheckWorks() throws Exception {
        val currency = randomString();
        val apiKey = randomString();
        when(apiKeyVault.getApiKey())
            .thenReturn(apiKey);

        http.expect(
                once(), requestTo(supportedCurrenciesUrl(apiKey))
            )
            .andExpect(method(GET))
            .andRespond(
                withStatus(OK)
                    .contentType(APPLICATION_JSON)
                    .body(randomCurrenciesResponse(currency))
            );

        val expected = true;
        val actual = service.isCurrencySupported(currency);
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

    private static String randomCurrenciesResponse(@NonNull String includingThis) throws JSONException {
        val currencies = generate(TestHelper::randomString)
            .limit(12)
            .collect(toSet());
        currencies.add(includingThis);

        val symbols = currencies.stream()
            .collect(toMap(identity(), currency -> "Description of " + currency));

        val json = new JSONObject()
            .put("success", true)
            .put("symbols", new JSONObject(symbols));
        return json.toString();
    }
}
