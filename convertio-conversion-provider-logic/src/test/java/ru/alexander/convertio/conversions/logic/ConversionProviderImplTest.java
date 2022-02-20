package ru.alexander.convertio.conversions.logic;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.alexander.convertio.conversions.api.ConversionFailedException;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.conversions.source.api.ConversionSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ru.alexander.convertio.test.helper.TestHelper.checkNotNull;
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
}
