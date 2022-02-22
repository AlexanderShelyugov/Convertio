package ru.alexander.convertio.conversions.logic;

import lombok.val;
import org.apache.logging.log4j.util.TriConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.alexander.convertio.conversions.api.ConversionFailedException;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.conversions.source.api.ConversionSource;
import ru.alexander.convertio.model.Money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.alexander.convertio.test.helper.TestHelper.checkNotNull;
import static ru.alexander.convertio.test.helper.TestHelper.randomAmount;
import static ru.alexander.convertio.test.helper.TestHelper.randomCurrency;
import static ru.alexander.convertio.test.helper.TestHelper.randomMoney;

@SpringBootTest(classes = {ConversionProviderImpl.class})
class ConversionProviderImplTest {

    @Autowired
    private ConversionProvider provider;

    @MockBean
    private ConversionSource conversionSource;

    @Test
    @DisplayName("Context loads")
    void testContext() {
        checkNotNull(provider, conversionSource);
    }

    @Test
    @DisplayName("Successful conversion works")
    void testSuccessfulConversion() throws Exception {
        val from = randomMoney();
        val to = randomMoney();

        when(conversionSource.convert(from, to.getCurrency()))
            .thenReturn(to);

        val expected = MoneyConversion.builder()
            .from(from)
            .to(to)
            .build();
        val actual = provider.convert(from, to.getCurrency());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Unsuccessful conversion works as expected")
    void testUnsuccessfulConversion() throws Exception {
        val from = randomMoney();
        val to = randomMoney();
        when(conversionSource.convert(from, to.getCurrency()))
            .thenThrow(Exception.class);
        assertThrows(ConversionFailedException.class, () ->
            provider.convert(from, to.getCurrency()));
    }

    @Test
    @DisplayName("Lowercase currencies are turned to uppercase")
    void testLowercaseCurrencies() throws Exception {
        val lowerCaseCurrency = randomCurrency().toLowerCase();
        val normalCurrency = randomCurrency();
        val amount = randomAmount();

        TriConsumer<String, String, Double> check = (from, to, money) -> {
            try {
                val expectedMoney = Money.builder().currency(from.toUpperCase()).amount(money).build();
                val expectedTo = to.toUpperCase();

                reset(conversionSource);
                when(conversionSource.convert(expectedMoney, expectedTo))
                    .thenReturn(Money.builder().currency(to).amount(randomAmount()).build());

                provider.convert(Money.builder().currency(from).amount(money).build(), to);

                verify(conversionSource, times(1))
                    .convert(expectedMoney, expectedTo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        check.accept(lowerCaseCurrency, normalCurrency, amount);
        check.accept(normalCurrency, lowerCaseCurrency, amount);
        check.accept(lowerCaseCurrency, normalCurrency.toLowerCase(), amount);
    }
}
