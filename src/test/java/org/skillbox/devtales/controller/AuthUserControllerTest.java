package org.skillbox.devtales.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.api.request.AuthRequest;
import org.skillbox.devtales.api.request.ChangePasswordRequest;
import org.skillbox.devtales.api.request.RegisterRequest;
import org.skillbox.devtales.config.Constants;
import org.skillbox.devtales.model.CaptchaCode;
import org.skillbox.devtales.model.GlobalSetting;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.repository.CaptchaRepository;
import org.skillbox.devtales.repository.GlobalSettingRepository;
import org.skillbox.devtales.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class AuthUserControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    CaptchaRepository captchaRepository;
    @Autowired
    GlobalSettingRepository globalSettingRepository;

    private User user;
    private CaptchaCode captcha;

    @BeforeEach
    public void setup() {
        super.setup();
        String email = "test@test.ru";
        String password = "123456";
        String name = "Arcadiy";
        LocalDateTime reg_date = LocalDateTime.now();
        String conf_code = "4321";

        user = new User()
                .setName(name)
                .setEmail(email)
                .setRegTime(reg_date)
                .setCode(conf_code)
                .setIsModerator(0)
                .setPassword(password);

        captcha = new CaptchaCode()
                .setCode("1234")
                .setSecretCode("4321")
                .setTime(LocalDateTime.now());

        captchaRepository.save(captcha);
    }

    @AfterEach
    public void cleanup() {
        captchaRepository.deleteAll();
    }

    @Test
    void registerTest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setPassword(user.getPassword())
                .setCaptcha(captcha.getCode())
                .setCaptchaSecret(captcha.getSecretCode());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        registerRequest
                .setEmail("testov1@test.ru")
                .setName("Testov")
                .setPassword("Testov321")
                .setCaptcha("1234")
                .setCaptchaSecret("4321");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();
        userRepository.delete(userRepository.findByEmail("testov1@test.ru").get());
    }

    @Test
    void badRegisterTest() throws Exception {
        Optional<GlobalSetting> multiuser_mode = globalSettingRepository.findByCode("MULTIUSER_MODE");
        multiuser_mode.get().setValue("YES");
        globalSettingRepository.save(multiuser_mode.get());

        RegisterRequest registerRequest = new RegisterRequest()
                .setEmail("testov1@test.ru")
                .setName("Testov")
                .setPassword("Testov321")
                .setCaptcha("1234")
                .setCaptchaSecret("4321");

        registerRequest.setPassword("123");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password")
                        .value(Constants.PASSWORD_ANSWER)).andReturn();

        registerRequest.setPassword("123456");
        registerRequest.setEmail("test@test.ru");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email")
                        .value(Constants.EMAIL_ANSWER)).andReturn();

        registerRequest.setPassword("123456")
                .setEmail("testov1@test.ru")
                .setCaptcha("1222");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.captcha")
                        .value(Constants.CAPTCHA_ANSWER)).andReturn();

        registerRequest.setPassword("123456")
                .setEmail("testov1@test.ru")
                .setCaptcha("1234")
                .setName("I");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name")
                        .value(Constants.NAME_ANSWER));
    }

    @Test
    void checkWithoutPrincipal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/check")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false));
    }

    @Test
    void loginTest() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(user.getEmail());
        authRequest.setPassword(user.getPassword());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();
    }

    @Test
    void badLoginTest() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(user.getEmail());
        authRequest.setPassword("111111");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false)).andReturn();

    }


    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void checkWithPrincipal() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/check")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id")
                        .value(userRepository.findByEmail("test@test.ru").get().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.moderation")
                        .value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.moderationCount")
                        .value(0));
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void logoutTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/auth/logout")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();

    }

    @Test
    void restorePasswordTest() throws Exception {

        String correctEmail = user.getEmail();
        String wrongEmail = 'A' + user.getEmail();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/restore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(correctEmail)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/restore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(wrongEmail)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false)).andReturn();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void changePasswordTest() throws Exception {
        String password = "123456";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setPassword(password);
        changePasswordRequest.setCode(userRepository.findByEmail(user.getEmail()).get().getCode());
        changePasswordRequest.setCaptcha(captcha.getCode());
        changePasswordRequest.setCaptchaSecret(captcha.getSecretCode());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/password")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true));

        changePasswordRequest.setCode(user.getCode());
        changePasswordRequest.setPassword("123");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/password")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password")
                        .value(Constants.PASSWORD_ANSWER));

        changePasswordRequest.setCode('1' + user.getCode());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/password")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(changePasswordRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}