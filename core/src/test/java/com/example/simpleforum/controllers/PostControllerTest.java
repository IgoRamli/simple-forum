package com.example.simpleforum.controllers;

import com.example.simpleforum.beans.AuthUserBean;
import com.example.simpleforum.beans.PostCreateRequestBean;
import com.example.simpleforum.generators.PostGenerator;
import com.example.simpleforum.model.Post;
import com.example.simpleforum.repositories.PostRepository;
import com.example.simpleforum.utils.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private AuthUtils authUtils;

    @SpyBean
    private ModelMapper modelMapper;

    @Autowired
    private WebApplicationContext context;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
    }

    @Test
    void whenUserCallsHealthCheck_shouldReturn200() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserGetPosts_shouldReturnAllPosts() throws Exception {
        List<Post> posts = PostGenerator.generateRandomListOfPosts(3);
        when(postRepository.findAll(100L)).thenReturn(posts);

        this.mockMvc.perform(get("/posts")
                        .queryParam("limit", "100"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postRepository).findAll(100L);
    }

    @Test
    @WithMockUser(roles = "user")
    void givenUserProvidesLastId_whenUserGetPosts_shouldReturnAllPosts() throws Exception {
        List<Post> posts = PostGenerator.generateRandomListOfPosts(3);
        when(postRepository.findAll(100L)).thenReturn(posts);

        this.mockMvc.perform(get("/posts")
                        .queryParam("limit", "100")
                        .queryParam("lastId", "5"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postRepository).findAll(100L, 5L);
    }

    @Test
    @WithMockUser(roles = "user")
    void givenUserProvidesKeyword_whenUserGetPosts_shouldReturnAllPosts() throws Exception {
        List<Post> posts = PostGenerator.generateRandomListOfPosts(3);
        when(postRepository.findByTitleKeyword("kw", 100L)).thenReturn(posts);

        this.mockMvc.perform(get("/posts")
                        .queryParam("keyword", "kw")
                        .queryParam("limit", "100"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postRepository).findByTitleKeyword("kw", 100L);
    }

    @Test
    @WithMockUser(roles = "user")
    void givenUserProvidesKeywordAndLastId_whenUserGetPosts_shouldReturnAllPosts() throws Exception {
        List<Post> posts = PostGenerator.generateRandomListOfPosts(3);
        when(postRepository.findByTitleKeyword("kw", 100L, 5L)).thenReturn(posts);

        this.mockMvc.perform(get("/posts")
                        .queryParam("keyword", "kw")
                        .queryParam("limit", "100")
                        .queryParam("lastId", "5"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postRepository).findByTitleKeyword("kw", 100L, 5L);
    }

    @Test
    void givenUserIsUnauthenticated_whenUserGetPosts_shouldReturn401() throws Exception {
        this.mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void givenUserDoesNotProvideLimit_whenUserGetPosts_shouldReturn400() throws Exception {
        this.mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserGetPostsById_shouldReturnPost() throws Exception {
        Post post = PostGenerator.generateRandomPost(3L);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        this.mockMvc.perform(get(String.format("/posts/%d", 3)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenUserIsUnauthenticated_whenUserGetPostById_shouldReturn401() throws Exception {
        this.mockMvc.perform(get(String.format("/posts/%d", 3)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void givenPostDoesNotExist_whenUserGetPostsById_shouldReturn404() throws Exception {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        this.mockMvc.perform(get(String.format("/posts/%d", 3)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserCreatesPost_ShouldReturn204() throws Exception {
        setUpJwtToken("thisUser");
        PostCreateRequestBean requestBody = PostCreateRequestBean.builder()
                .title("Sample Title")
                .content("Sample content")
                .build();
        when(postRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        this.mockMvc.perform(post("/posts")
                        .with(csrf())
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(postRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void givenUserIsUnauthenticated_whenUserCreatesPost_ShouldReturn401() throws Exception {
        this.mockMvc.perform(post("/posts").with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserDeletePost_shouldReturn204() throws Exception {
        setUpJwtToken("thisUser");

        Post post = PostGenerator.generateRandomPost(3L, "thisUser");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        this.mockMvc.perform(delete(String.format("/posts/%d", 3)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(postRepository).deleteById(3L);
    }

    @Test
    void givenUserIsUnauthenticated_whenUserDeletePosts_shouldReturn401() throws Exception {
        this.mockMvc.perform(delete(String.format("/posts/%d", 3)).with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    void givenPostDoesNotExist_whenUserDeletePost_shouldReturn404() throws Exception {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        this.mockMvc.perform(delete(String.format("/posts/%d", 3)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    @WithMockUser(roles = "user")
    void givenPostIsFromAnotherUser_whenUserDeletePost_shouldReturn403() throws Exception {
        setUpJwtToken("thisUser");

        Post post = PostGenerator.generateRandomPost(3L, "anotherUser");
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        this.mockMvc.perform(delete(String.format("/posts/%d", 3)).with(csrf()).with(authentication(authentication)))
                .andDo(print())
                .andExpect(status().isForbidden());

        verify(postRepository, never()).deleteById(anyLong());
    }

    void setUpJwtToken(String userId) {
        Jwt jwt = mock(Jwt.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(userId);
        when(authUtils.getAuthDetails(any())).thenReturn(
                AuthUserBean.builder()
                        .id(userId)
                        .build());
    }
}
