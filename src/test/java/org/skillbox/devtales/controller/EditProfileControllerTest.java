package org.skillbox.devtales.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.api.request.EditProfileRequest;
import org.skillbox.devtales.api.request.EditProfileWithPhotoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;

import static org.skillbox.devtales.config.Constants.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class EditProfileControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void editProfileWithPhotoTest() throws Exception {

        FileInputStream fis = new FileInputStream("D:/FolderTo/1_9.jpg");
        MockMultipartFile image = new MockMultipartFile("image", "test.png",
                "image/png", fis.readAllBytes());

        EditProfileWithPhotoRequest editProfileRequest = new EditProfileWithPhotoRequest()
                .setName("User Testov")
                .setPassword("123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editProfileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true));

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void editProfileWithoutPhotoTest() throws Exception {

        EditProfileRequest editProfileRequest = new EditProfileRequest()
                .setName("User Testov")
                .setPassword("123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editProfileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();

        editProfileRequest.setPassword("123");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editProfileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value(PASSWORD_ANSWER)).andReturn();

        editProfileRequest.setName("A");
        editProfileRequest.setPassword("123456");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editProfileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value(NAME_ANSWER)).andReturn();

        editProfileRequest.setName("User Testov");
        editProfileRequest.setEmail("mailone@example.ru");
        editProfileRequest.setPassword("123456");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editProfileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value(EMAIL_ANSWER)).andReturn();

        editProfileRequest.setName("User Testov");
        editProfileRequest.setEmail("! * /@example.ru");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editProfileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value(EMAIL_FORMAT_ANSWER)).andReturn();


    }
}
