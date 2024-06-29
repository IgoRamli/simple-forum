package com.example.simpleforum.controllers;

import com.example.simpleforum.beans.AuthUserBean;
import com.example.simpleforum.beans.GetVotesResponseBean;
import com.example.simpleforum.model.Vote;
import com.example.simpleforum.model.VoteId;
import com.example.simpleforum.repositories.VoteRepository;
import com.example.simpleforum.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VoteController {
    private final VoteRepository voteRepository;

    private final AuthUtils authUtils;

    @Autowired
    public VoteController(VoteRepository voteRepository, AuthUtils authUtils) {
        this.voteRepository = voteRepository;
        this.authUtils = authUtils;
    }

    @GetMapping("/posts/{postId}/votes")
    public GetVotesResponseBean getVotes(@PathVariable Long postId, Authentication authentication) {
        return getVoteStats(postId, authentication);
    }

    /*
    Upvotes a post as user authenticated
    If user already upvoted the given post, no changes happened and server will return 200 regardless
    If user already downvoted, the upvote is overridden with an upvote
     */
    @PostMapping("/posts/{postId}/votes/upvotes")
    public GetVotesResponseBean addUpvote(@PathVariable Long postId, Authentication authentication) {
        AuthUserBean user = authUtils.getAuthDetails(authentication);

        voteRepository.saveAndFlush(Vote.builder()
                .userId(user.getId())
                .postId(postId)
                .type("UP")
                .build());

        return getVoteStats(postId, authentication);
    }

    /*
    Downvotes a post as user authenticated
    If user already downvoted the given post, no changes happened and server will return 200 regardless
    If user already upvoted, the upvote is overridden with a downvote
     */
    @PostMapping("/posts/{postId}/votes/downvotes")
    public GetVotesResponseBean addDownvote(@PathVariable Long postId, Authentication authentication) {
        AuthUserBean user = authUtils.getAuthDetails(authentication);

        voteRepository.saveAndFlush(Vote.builder()
                .userId(user.getId())
                .postId(postId)
                .type("DOWN")
                .build());

        return getVoteStats(postId, authentication);
    }

    @DeleteMapping({"/posts/{postId}/votes"})
    public GetVotesResponseBean retractVote(@PathVariable Long postId, Authentication authentication) {
        AuthUserBean user = authUtils.getAuthDetails(authentication);

        voteRepository.deleteById(new VoteId(user.getId(), postId));

        return getVoteStats(postId, authentication);
    }

    private GetVotesResponseBean getVoteStats(Long postId, Authentication authentication) {
        GetVotesResponseBean voteStats = GetVotesResponseBean.builder()
                .postId(postId)
                .upvotes(voteRepository.countUpvotesByPostId(postId).orElse(0L))
                .downvotes(voteRepository.countDownvotesByPostId(postId).orElse(0L))
                .build();

        if (authentication.isAuthenticated()) {
            AuthUserBean user = authUtils.getAuthDetails(authentication);
            Optional<String> userVote = voteRepository.getVote(postId, user.getId());
            voteStats.setIsUpvoted(userVote.filter(s -> s.equals("UP")).isPresent());
            voteStats.setIsDownvoted(userVote.filter(s -> s.equals("DOWN")).isPresent());
        }
        return voteStats;
    }
}
