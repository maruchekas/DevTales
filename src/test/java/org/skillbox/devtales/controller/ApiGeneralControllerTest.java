package org.skillbox.devtales.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiGeneralControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @AfterEach
    public void cleanup() {
    }

    @Test
    void initTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/init")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("DevTales"));
    }

    @Test
    void tagTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tag")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void calendarTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/calendar")
                        .contentType(MediaType.APPLICATION_JSON).param("year", "2022"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}