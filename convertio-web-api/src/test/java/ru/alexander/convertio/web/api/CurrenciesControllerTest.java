package ru.alexander.convertio.web.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.alexander.convertio.conversions.api.CurrenciesService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alexander.convertio.test.helper.TestHelper.randomCurrenciesWithDescriptions;

@WebMvcTest(CurrenciesController.class)
class CurrenciesControllerTest {
    private static final String ENDPOINT = "/currencies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CurrenciesService currenciesService;

    @Test
    @DisplayName("Context loads successfully!")
    void contextLoads() {
        asList(mockMvc, currenciesService)
            .forEach(o -> assertThat(o, is(notNullValue())));
    }

    @Test
    @DisplayName("Supported currencies are displayed")
    void getSupportedCurrencies() throws Exception {
        val expected = randomCurrenciesWithDescriptions();
        when(currenciesService.getAllSupportedCurrencies())
            .thenReturn(expected);

        val result = mockMvc.perform(
                get(ENDPOINT).accept(APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn();
        val actual = extractCurrencies(result);
        assertThat(actual, is(notNullValue()));
        assertEquals(expected, actual);
    }

    private Map<String, String> extractCurrencies(@NonNull MvcResult r) throws Exception {
        val typeRef = new TypeReference<HashMap<String, String>>() {
        };
        return mapper.readValue(r.getResponse().getContentAsString(), typeRef);
    }
}