package ru.alexander.convertio.conversions.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import ru.alexander.convertio.conversions.api.ConversionProvider;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(classes = {ConversionProviderImpl.class})
class ConversionProviderImplTest {
    @Autowired
    private ConversionProvider provider;

    @MockBean
    private ApiKeyVault apiKey;

    @MockBean
    private RestTemplate http;

    @Test
    @DisplayName("Context loads")
    void testContext() {
        asList(provider, apiKey, http)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Successful conversion works")
    void testSuccessfulConversion() {

    }
}
