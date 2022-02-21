package org.skillbox.devtales.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skillbox.devtales.AbstractTest;
import org.skillbox.devtales.api.request.PostRequest;
import org.skillbox.devtales.api.request.VoteRequest;
import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.model.enums.ModerationStatus;
import org.skillbox.devtales.repository.PostRepository;
import org.skillbox.devtales.repository.PostVoteRepository;
import org.skillbox.devtales.repository.UserRepository;
import org.skillbox.devtales.service.AuthUserService;
import org.skillbox.devtales.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.skillbox.devtales.config.Constants.TEXT_ANSWER;
import static org.skillbox.devtales.config.Constants.TITLE_ANSWER;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiPostControllerTest extends AbstractTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostVoteRepository postVoteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthUserService userService;

    @Autowired
    MockMvc mockMvc;

    private Pageable pageable;
    private Page<Post> newest;
    private Page<Post> popular;
    private Page<Post> best;
    private Page<Post> early;

    @BeforeEach
    public void setup() {
        super.setup();
        pageable = PageRequest.of(0 / 10, 10);
        newest = postRepository.findRecentPostsSortedByDate(pageable);
        popular = postRepository.findPopularPostsSortedByCommentsCount(pageable);
        best = postRepository.findBestPostsSortedByLikeCount(pageable);
        early = postRepository.findEarlyPostsSortedByDate(pageable);
    }

    @AfterEach
    public void cleanup() {

    }

    @Test
    void getPageOfPostsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(newest.getTotalElements()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "popular"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(popular.getTotalElements()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "best"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(best.getTotalElements()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "early"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(early.getTotalElements()));
    }

    @Test
    void getOnePostByIdTest() throws Exception {
        Post post = postRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/{id}", 200)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        post.setModerationStatus(ModerationStatus.DECLINED);
        postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        postRepository.save(post);

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void addPostTest() throws Exception {

        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Title for post");
        postRequest.setText("a simple text for post containing more than fifty characters bla-bla-bla");
        postRequest.setTimestamp(DateTimeUtil.getTimestamp(LocalDateTime.now()));
        postRequest.setActive((byte) 1);
        postRequest.setTags(new String[0]);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.status().isOk());

        postRepository.delete(postRepository.findAll()
                .get(postRepository.findAll().size() - 1));
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void badAddPostTest() throws Exception {

        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Title");
        postRequest.setText("a simple text for post containing more than fifty characters bla-bla-bla");
        postRequest.setTimestamp(DateTimeUtil.getTimestamp(LocalDateTime.now()));
        postRequest.setActive((byte) 1);
        postRequest.setTags(new String[0]);

        postRequest.setTitle("Ti");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.title").value(TITLE_ANSWER))
                .andExpect(MockMvcResultMatchers.status().isOk());

        postRequest.setTitle("Title");
        postRequest.setText("short text");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.text").value(TEXT_ANSWER))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void addPostByUnauthorisedUser() throws Exception {

        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Title");
        postRequest.setText("a simple text for post containing more than fifty characters bla-bla-bla");
        postRequest.setTimestamp(DateTimeUtil.getTimestamp(LocalDateTime.now()));
        postRequest.setActive((byte) 1);
        postRequest.setTags(new String[0]);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void editPostTest() throws Exception {

        int id = postRepository.findAll().get(0).getId();
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Edited title for post");
        postRequest.setText("Edited text for post containing more than fifty characters bla-bla-bla");
        postRequest.setTimestamp(DateTimeUtil.getTimestamp(LocalDateTime.now()));
        postRequest.setActive((byte) 1);
        postRequest.setTags(new String[0]);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/post/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void badEditPostTest() throws Exception {

        int id = 1;
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Edited title for post");
        postRequest.setText("Edited text for post containing more than fifty characters bla-bla-bla");
        postRequest.setTimestamp(DateTimeUtil.getTimestamp(LocalDateTime.now()));
        postRequest.setActive((byte) 1);
        postRequest.setTags(new String[0]);


        postRequest.setTitle(" ");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/post/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.title").value(TITLE_ANSWER))
                .andExpect(MockMvcResultMatchers.status().isOk());

        postRequest.setText("Edited short text");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/post/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.errors.title").value(TITLE_ANSWER),
                        MockMvcResultMatchers.jsonPath("$.errors.text").value(TEXT_ANSWER))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void editPostByUnauthorisedUser() throws Exception {

        int id = 1;
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Edited title for post");
        postRequest.setText("Edited text for post containing more than fifty characters bla-bla-bla");
        postRequest.setTimestamp(DateTimeUtil.getTimestamp(LocalDateTime.now()));
        postRequest.setActive((byte) 1);
        postRequest.setTags(new String[0]);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/post/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "mailone@example.ru", authorities = "user:write")
    void getMyPostsTest() throws Exception {

        User user = userRepository.findAll().get(0);
        Page<Post> published = postRepository.findMyPosts(new String[]{"ACCEPTED"}, user.getId(), (byte) 1, pageable);
        Page<Post> active = postRepository.findMyPosts(new String[]{"NEW"}, user.getId(), (byte) 1, pageable);
        Page<Post> declined = postRepository.findMyPosts(new String[]{"DECLINED"}, user.getId(), (byte) 1, pageable);
        Page<Post> inactiveAccepted = postRepository.findMyPosts(new String[]{"ACCEPTED", "NEW"}, user.getId(), (byte) 0, pageable);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .principal(user::getEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "published"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(published.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .principal(user::getEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "pending"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(active.getTotalElements()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .principal(user::getEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "declined"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(declined.getTotalElements()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .principal(user::getEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "inactive"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(inactiveAccepted.getTotalElements()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:moderate")
    void getPostsForModerationTest() throws Exception {

        User user = userService.getUserByEmail("test@test.ru");
        user.setIsModerator(1);
        userRepository.save(user);

        Page<Post> accepted = postRepository.findNewPostsForModeration("accepted", pageable);
        Page<Post> declined = postRepository.findNewPostsForModeration("declined", pageable);
        Page<Post> newPosts = postRepository.findNewPostsForModeration("declined", pageable);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(user::getEmail).contentType(MediaType.APPLICATION_JSON)
                        .param("status", "accepted"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(accepted.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(user::getEmail).contentType(MediaType.APPLICATION_JSON)
                        .param("status", "declined"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(declined.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(user::getEmail).contentType(MediaType.APPLICATION_JSON)
                        .param("status", "newPosts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(newPosts.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        user.setIsModerator(0);
        userRepository.save(user);

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void getPostsForModerationByNoModeratorTest() throws Exception {

        User user = userService.getUserByEmail("test@test.ru");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(user::getEmail).contentType(MediaType.APPLICATION_JSON)
                        .param("status", "newPosts"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    void getPostsByTextTest() throws Exception {

        String text = "ipsum";
        Page<Post> foundPosts = postRepository.findPostsByText(text, pageable);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query", text))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(foundPosts.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        text = "шзыгь";
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query", text))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(foundPosts.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query", "какой-то текст, которого тоно нет"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void getPostsByTagTest() throws Exception {

        String tag = "Java";
        Page<Post> foundPosts = postRepository.findPostsByTag(tag, pageable);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byTag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tag", tag))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(foundPosts.getTotalElements()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byTag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tag", "noSuchTag"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void getPostsByDateTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", "2022-02-10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", "2022-01-01"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void putLikeTest() throws Exception {

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPostId(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "published").principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(voteRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/like")
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(voteRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.status().isOk());

        postVoteRepository.delete(postVoteRepository.findAll()
                .get(postVoteRepository.findAll().size() - 1));

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void putDislikeTest() throws Exception {

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPostId(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/dislike")
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(voteRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/dislike")
                        .contentType(MediaType.APPLICATION_JSON).principal(() -> "test@test.ru")
                        .content(mapper.writeValueAsString(voteRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.status().isOk());

        postVoteRepository.delete(postVoteRepository.findAll()
                .get(postVoteRepository.findAll().size() - 1));

    }

}
