package com.example.simpleforum.controllers;

import com.example.simpleforum.beans.AuthUserBean;
import com.example.simpleforum.model.Vote;
import com.example.simpleforum.model.VoteId;
import com.example.simpleforum.repositories.VoteRepository;
import com.example.simpleforum.utils.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteRepository voteRepository;

    @MockBean
    private AuthUtils authUtils;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
    }

    @Test
    @WithMockUser(roles = "user")
    void givenUserHasUpvoted_whenUserGetVotes_shouldReturnTotalVoteAndWhetherUserHasUpvoted() throws Exception {
        setUpJwtToken("thisUser");

        Long postId = 1L;
        when(voteRepository.getVote(any(), any())).thenReturn(Optional.of("UP"));
        when(voteRepository.countUpvotesByPostId(any())).thenReturn(Optional.of(5L));
        when(voteRepository.countDownvotesByPostId(any())).thenReturn(Optional.of(3L));

        this.mockMvc.perform(get(String.format("/posts/%d/votes", postId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value("1"))
                .andExpect(jsonPath("$.upvotes").value("5"))
                .andExpect(jsonPath("$.downvotes").value("3"))
                .andExpect(jsonPath("$.isUpvoted").value("true"))
                .andExpect(jsonPath("$.isDownvoted").value("false"));
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserAddUpvote_shouldVerifyThatAddUpvoteMethodIsCalled() throws Exception {
        setUpJwtToken("thisUser");

        Long postId = 1L;
        when(voteRepository.getVote(any(), any())).thenReturn(Optional.of("UP"));
        when(voteRepository.countUpvotesByPostId(any())).thenReturn(Optional.of(5L));
        when(voteRepository.countDownvotesByPostId(any())).thenReturn(Optional.of(3L));

        this.mockMvc.perform(post(String.format("/posts/%d/votes/upvotes", postId)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value("1"))
                .andExpect(jsonPath("$.upvotes").value("5"))
                .andExpect(jsonPath("$.downvotes").value("3"))
                .andExpect(jsonPath("$.isUpvoted").value("true"))
                .andExpect(jsonPath("$.isDownvoted").value("false"));

        ArgumentCaptor<Vote> saveArgCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository, times(1)).saveAndFlush(saveArgCaptor.capture());
        Vote saveArg = saveArgCaptor.getValue();
        assertThat(saveArg.getUserId()).isEqualTo("thisUser");
        assertThat(saveArg.getPostId()).isEqualTo(postId);
        assertThat(saveArg.getType()).isEqualTo("UP");
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserAddDownvote_shouldVerifyThatAddDownvoteMethodIsCalled() throws Exception {
        setUpJwtToken("thisUser");

        Long postId = 1L;
        when(voteRepository.getVote(any(), any())).thenReturn(Optional.of("DOWN"));
        when(voteRepository.countUpvotesByPostId(any())).thenReturn(Optional.of(5L));
        when(voteRepository.countDownvotesByPostId(any())).thenReturn(Optional.of(3L));

        this.mockMvc.perform(post(String.format("/posts/%d/votes/downvotes", postId)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value("1"))
                .andExpect(jsonPath("$.upvotes").value("5"))
                .andExpect(jsonPath("$.downvotes").value("3"))
                .andExpect(jsonPath("$.isUpvoted").value("false"))
                .andExpect(jsonPath("$.isDownvoted").value("true"));

        ArgumentCaptor<Vote> saveArgCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository, times(1)).saveAndFlush(saveArgCaptor.capture());
        Vote saveArg = saveArgCaptor.getValue();
        assertThat(saveArg.getUserId()).isEqualTo("thisUser");
        assertThat(saveArg.getPostId()).isEqualTo(postId);
        assertThat(saveArg.getType()).isEqualTo("DOWN");
    }

    @Test
    @WithMockUser(roles = "user")
    void whenUserRemoveVote_shouldVerifyThatRetractVoteMethodIsCalled() throws Exception {
        setUpJwtToken("thisUser");

        Long postId = 1L;
        when(voteRepository.getVote(any(), any())).thenReturn(Optional.empty());
        when(voteRepository.countUpvotesByPostId(any())).thenReturn(Optional.of(5L));
        when(voteRepository.countDownvotesByPostId(any())).thenReturn(Optional.of(3L));

        this.mockMvc.perform(delete(String.format("/posts/%d/votes", postId)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value("1"))
                .andExpect(jsonPath("$.upvotes").value("5"))
                .andExpect(jsonPath("$.downvotes").value("3"))
                .andExpect(jsonPath("$.isUpvoted").value("false"))
                .andExpect(jsonPath("$.isDownvoted").value("false"));

        verify(voteRepository, times(1))
                .deleteById(new VoteId("thisUser", postId));
    }

    void setUpJwtToken(String userId) {
        Jwt jwt = mock(Jwt.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwt.getSubject()).thenReturn(userId);
        when(authUtils.getAuthDetails(any())).thenReturn(
                AuthUserBean.builder()
                        .id(userId)
                        .build());
    }
}
