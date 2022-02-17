package ru.alexander.convertio.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alexander.convertio.web.api.helper.TestHelper.randomAmount;
import static ru.alexander.convertio.web.api.helper.TestHelper.randomString;

@WebMvcTest(ConversionsController.class)
class ConversionsControllerTests {
    private static final String ENDPOINT = "/conversions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Context loads successfully!")
    void contextLoads() {
        assertThat(mockMvc, is(notNullValue()));
        assertThat(mapper, is(notNullValue()));
    }

    @Test
    @DisplayName("Basic conversions endpoint works fine")
    void basicConversionsWork() throws Exception {
        val sourceCurrency = randomString();
        val targetCurrency = randomString();
        val sourceAmount = randomAmount();
        val expectedTargetAmount = randomAmount();

        val r = convert(sourceCurrency, targetCurrency, sourceAmount)
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn();

        val actual = mapper.readValue(
            r.getResponse().getContentAsString(),
            ConversionResult.class
        );
        assertThat(actual, is(notNullValue()));
        val expected = ConversionResult.builder()
            .sourceCurrency(sourceCurrency)
            .sourceAmount(sourceAmount)
            .targetCurrency(targetCurrency)
            .targetAmount(expectedTargetAmount)
            .build();
        assertEquals(expected, actual);
    }

    private ResultActions convert(String sCurrency, String tCurrency, double sAmount) throws Exception {
        return mockMvc.perform(
            get(conversionURI(sCurrency, tCurrency, sAmount))
                .accept(APPLICATION_JSON)
        );
    }

    private static String conversionURI(String sCurrency, String tCurrency, double sAmount) {
        return String.join("/", ENDPOINT, sCurrency, tCurrency, String.valueOf(sAmount));
    }

}
