package ru.alexander.convertio.web.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@WebMvcTest(ConvertionsController.class)
class ConvertionsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Context loads successfully!")
    public void contextLoads() {
        assertThat(mockMvc, is(notNullValue()));
    }

}
