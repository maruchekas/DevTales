package org.skillbox.devtales.controller;

import org.apache.http.entity.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.api.request.EditProfileWithPhotoRequest;
import org.skillbox.devtales.api.request.EditSettingsRequest;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.UserRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import static org.skillbox.devtales.config.Constants.CAPTCHA_IMAGE_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiGeneralControllerTest extends AbstractTest {

    private User user;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        super.setup();
        user = userRepository.findByEmail("test@test.ru").get();
    }

    @AfterEach
    public void cleanup() {
        user.setIsModerator(0);
        userRepository.save(user);
    }

    @Test
    void initTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/init")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("DevTales"));
    }

    @Test
    void tagTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tag")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(MockMvcResultMatchers.content().string(Matchers.containsString("name")),
                        MockMvcResultMatchers.content().string(Matchers.containsString("weight")))
                .andExpect(status().isOk());
    }

    @Test
    void calendarTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/calendar")
                        .contentType(MediaType.APPLICATION_JSON).param("year", "2022"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:moderate")
    void editSettingsTest() throws Exception {
        EditSettingsRequest editSettingsRequest = new EditSettingsRequest()
                .setMultiuserMode(true)
                .setPostPremoderation(false)
                .setStatisticsIsPublic(true);

        user.setIsModerator(1);
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/settings")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editSettingsRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.MULTIUSER_MODE").value(true),
                        MockMvcResultMatchers.jsonPath("$.POST_PREMODERATION").value(false),
                        MockMvcResultMatchers.jsonPath("$.STATISTICS_IS_PUBLIC").value(true))
                .andExpect(status().isOk());
    }

    @Test
    void getSettingsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/settings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.MULTIUSER_MODE").value(true),
                        MockMvcResultMatchers.jsonPath("$.POST_PREMODERATION").value(false),
                        MockMvcResultMatchers.jsonPath("$.STATISTICS_IS_PUBLIC").value(true))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:moderate")
    void editSettingsWithNoModeratorTest() throws Exception {
        EditSettingsRequest editSettingsRequest = new EditSettingsRequest()
                .setMultiuserMode(true)
                .setPostPremoderation(false)
                .setStatisticsIsPublic(true);

        user.setIsModerator(0);
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/settings")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editSettingsRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void uploadImageTest() throws Exception {

        FileInputStream fis = new FileInputStream("src/main/resources/static/default-1.png");
        MockMultipartFile image = new MockMultipartFile("image","test.png",
                "image/png", fis.readAllBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/image").file(image)
                        .principal(() -> "test@test.ru"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}