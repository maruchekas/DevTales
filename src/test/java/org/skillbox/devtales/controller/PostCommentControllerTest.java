package org.skillbox.devtales.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.api.request.PostCommentRequest;
import org.skillbox.devtales.model.PostComment;
import org.skillbox.devtales.repository.CommentRepository;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.skillbox.devtales.config.Constants.TEXT_COMMENT_ANSWER;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class PostCommentControllerTest extends AbstractTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthUserService userService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @AfterEach
    public void cleanup() {
        PostComment comment = commentRepository.findAll()
                .get(commentRepository.findAll().size() - 1);
        commentRepository.delete(comment);
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void addCommentTest() throws Exception {
        PostCommentRequest postCommentRequest = new PostCommentRequest();
        postCommentRequest.setText("a simple comment text containing more than twenty characters");
        postCommentRequest.setPostId(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postCommentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        postCommentRequest.setParentId(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postCommentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void addBadCommentTest() throws Exception {
        PostCommentRequest postCommentRequest = new PostCommentRequest();
        postCommentRequest.setText("a simple comment text containing more than twenty characters");
        postCommentRequest.setPostId(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postCommentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        postCommentRequest.setText("short text");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postCommentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.text").value(TEXT_COMMENT_ANSWER));

        postCommentRequest.setText("a simple comment text containing more than twenty characters");
        postCommentRequest.setPostId(1000);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postCommentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        postCommentRequest.setParentId(1000);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postCommentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
