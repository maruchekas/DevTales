package org.skillbox.devtales.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.api.request.ModeratePostRequest;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ModerateControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthUserService userService;

    @Autowired
    PostRepository postRepository;

    private User user;

    @BeforeEach
    public void setup() {
        super.setup();
        user = userService.getUserByEmail("test@test.ru");
    }

    @AfterEach
    public void cleanup() {
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:moderate")
    void moderatePostTest() throws Exception {
        user.setIsModerator(1);
        userRepository.save(user);

        ModeratePostRequest moderatePostRequest = new ModeratePostRequest()
                .setPostId(1)
                .setDecision("decline");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/moderation")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(moderatePostRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true));

        moderatePostRequest.setDecision("accept");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/moderation")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(moderatePostRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true));

        user.setIsModerator(0);
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/moderation")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(moderatePostRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false));


    }
}
