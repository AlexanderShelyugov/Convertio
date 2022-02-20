package ru.alexander.convertio.conversions.logic;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.alexander.convertio.conversions.api.CurrenciesService;
import ru.alexander.convertio.conversions.source.api.CurrenciesSource;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.alexander.convertio.test.helper.TestHelper.randomCurrencies;
import static ru.alexander.convertio.test.helper.TestHelper.randomCurrenciesWithDescriptions;
import static ru.alexander.convertio.test.helper.TestHelper.randomCurrency;

@SpringBootTest(classes = {CurrenciesServiceImpl.class})
class CurrenciesServiceImplTest {
    @Autowired
    private CurrenciesService service;

    @MockBean
    private CurrenciesSource currenciesSource;

    @Test
    @DisplayName("Context loads")
    void testContext() {
        asList(service, currenciesSource)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Basic currency check works fine")
    void testSupportedCurrencyCheck() {
        val currency = randomCurrency();
        when(currenciesSource.supportedCurrencies())
            .thenReturn(randomCurrencies(currency));

        val expected = true;
        val actual = service.isCurrencySupported(currency);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Basic not supported currency check works fine")
    void testNotSupportedCurrencyCheck() {
        val currency = randomCurrency();
        when(currenciesSource.supportedCurrencies())
            .thenReturn(randomCurrencies());

        val expected = false;
        val actual = service.isCurrencySupported(currency);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Currencies with description works fine")
    void testCurrenciesWithDescriptions() {
        val expected = randomCurrenciesWithDescriptions();
        when(currenciesSource.currenciesWithDescription())
            .thenReturn(expected);
        val actual = service.getAllSupportedCurrencies();
        assertThat(actual, is(notNullValue()));
        assertEquals(expected, actual);
    }
}
