package ru.alexander.convertio.web.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversionsController.class)
class ConversionsControllerTests {
    private static final String ENDPOINT = "/conversions";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Context loads successfully!")
    void contextLoads() {
        assertThat(mockMvc, is(notNullValue()));
    }

    @Test
    @DisplayName("Basic conversions endpoint works fine")
    void basicConversionsWork() throws Exception {
        mockMvc.perform(
                get(ENDPOINT)
                    .accept(APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON));
    }

}
