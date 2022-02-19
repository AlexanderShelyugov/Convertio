package ru.alexander.convertio.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.CurrenciesService;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alexander.convertio.test.helper.TestHelper.randomAmount;
import static ru.alexander.convertio.test.helper.TestHelper.randomMoney;
import static ru.alexander.convertio.test.helper.TestHelper.randomString;

@WebMvcTest(ConversionsController.class)
class ConversionsControllerTests {
    private static final String ENDPOINT = "/conversions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ConversionProvider provider;

    @MockBean
    private CurrenciesService currenciesService;

    @BeforeEach
    void prepare() {
        when(currenciesService.isCurrencySupported(notNull()))
            .thenReturn(true);
    }

    @Test
    @DisplayName("Context loads successfully!")
    void contextLoads() {
        asList(mockMvc, mapper, provider, currenciesService)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Basic conversion works fine")
    void basicConversionsWork() throws Exception {
        val from = randomMoney();
        val to = randomMoney();

        when(provider.convert(from, to.getCurrency()))
            .thenReturn(
                MoneyConversion.builder()
                    .from(from)
                    .to(to)
                    .build()
            );

        val r = convert(from.getCurrency(), to.getCurrency(), from.getAmount())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn();

        val actual = extractFrom(r);
        assertThat(actual, is(notNullValue()));

        val expected = ConversionResult.builder()
            .sourceCurrency(from.getCurrency())
            .sourceAmount(from.getAmount())
            .targetCurrency(to.getCurrency())
            .targetAmount(to.getAmount())
            .build();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Request with unsupported currencies is bad")
    void unsupportedCurrrenciesAreDeclined() throws Exception {
        val from = randomMoney();
        val toCurrency = randomString();

        when(currenciesService.isCurrencySupported(from.getCurrency()))
            .thenReturn(false);
        when(currenciesService.isCurrencySupported(toCurrency))
            .thenReturn(true);
        convert(from.getCurrency(), toCurrency, from.getAmount())
            .andExpect(status().isBadRequest());

        reset(currenciesService);
        when(currenciesService.isCurrencySupported(from.getCurrency()))
            .thenReturn(true);
        when(currenciesService.isCurrencySupported(toCurrency))
            .thenReturn(false);
        convert(from.getCurrency(), toCurrency, from.getAmount())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Requests with negative amount are bad")
    void negativeAmountIsDeclined() throws Exception {
        val amount = -1 * randomAmount();
        assertTrue(amount < 0);
        convert(randomString(), randomString(), amount)
            .andExpect(status().isBadRequest());
    }

    private ResultActions convert(String sCurrency, String tCurrency, double sAmount) throws Exception {
        mockMvc.getDispatcherServlet().setThrowExceptionIfNoHandlerFound(false);
        return mockMvc.perform(
            get(conversionURI(sCurrency, tCurrency, sAmount))
                .accept(APPLICATION_JSON)
        );
    }

    private ConversionResult extractFrom(@NonNull MvcResult r) throws Exception {
        return mapper.readValue(
            r.getResponse().getContentAsString(),
            ConversionResult.class
        );
    }

    private static String conversionURI(String sCurrency, String tCurrency, double sAmount) {
        return String.join("/", ENDPOINT, sCurrency, tCurrency, String.valueOf(sAmount));
    }

}
