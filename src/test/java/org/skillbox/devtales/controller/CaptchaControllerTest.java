package org.skillbox.devtales.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.repository.CaptchaRepository;
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
public class CaptchaControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CaptchaRepository captchaRepository;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @AfterEach
    public void cleanup() {
        captchaRepository.deleteAll();
    }

    @Test
    void getCaptchaTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(("/api/auth/captcha"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(MockMvcResultMatchers.content().string(Matchers.containsString("secret")),
                        MockMvcResultMatchers.content().string(Matchers.containsString("image")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void creatingCaptchaTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/captcha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.secret")
                        .value(captchaRepository.findLastUpdating().get(0).getSecretCode()));
    }
}
