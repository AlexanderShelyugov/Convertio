package ru.alexander.convertio.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alexander.convertio.web.api.helper.TestHelper.randomMoney;

@WebMvcTest(ConversionsController.class)
class ConversionsControllerTests {
    private static final String ENDPOINT = "/conversions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ConversionProvider provider;

    @AfterEach
    void checkAfter() {
        verifyNoMoreInteractions(provider);
    }

    @Test
    @DisplayName("Context loads successfully!")
    void contextLoads() {
        asList(mockMvc, mapper, provider)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Basic conversions endpoint works fine")
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

    private ResultActions convert(String sCurrency, String tCurrency, double sAmount) throws Exception {
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
