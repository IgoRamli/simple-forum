package com.example.simpleforum.repositories;

import com.example.simpleforum.model.Vote;
import com.example.simpleforum.model.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface VoteCountByPostId {
    Long getPostId();
    Long getVoteCount();
}

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    // If no votes found, this return NULL
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.postId = ?1 AND type='UP' GROUP BY v.postId")
    Optional<Long> countUpvotesByPostId(Long postId);

    // If no votes found, this return NULL
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.postId IN ?1 AND type='DOWN' GROUP BY v.postId")
    Optional<Long> countDownvotesByPostId(Long postId);

    @Query("SELECT v.type FROM Vote v WHERE v.postId = ?1 AND v.userId = ?2")
    Optional<String> getVote(Long postId, String userId);

    @Query("SELECT v.postId AS postId, COUNT(v) AS voteCount FROM Vote v WHERE v.postId IN ?1 AND type='UP' GROUP BY v.postId")
    List<VoteCountByPostId> countUpvotesByPostIds(List<Long> postIds);

    @Query("SELECT v.postId AS postId, COUNT(v) AS voteCount FROM Vote v WHERE v.postId IN ?1 AND type='DOWN' GROUP BY v.postId")
    List<VoteCountByPostId> countDownvotesByPostIds(List<Long> postIds);


}
