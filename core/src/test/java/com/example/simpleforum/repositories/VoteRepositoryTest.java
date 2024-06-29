package com.example.simpleforum.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql({"/db/votes.sql"})
public class VoteRepositoryTest {
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void givenVotes_whenCountUpvotesByPostId_thenReturnTotalUpvotes() {
        Long postId = 1L;

        Optional<Long> actual = voteRepository.countUpvotesByPostId(postId);

        assertThat(actual).hasValue(2L);
    }

    @Test
    void givenVotes_whenCountDownvotesByPostId_thenReturnTotalDownvotes() {
        Long postId = 1L;

        Optional<Long> actual = voteRepository.countDownvotesByPostId(postId);

        assertThat(actual).hasValue(1L);
    }

    @Test
    void givenNoVotes_whenCountUpvotesByPostId_thenReturnZero() {
        Optional<Long> actual = voteRepository.countUpvotesByPostId(3L);

        assertThat(actual).isEmpty();
    }

    @Test
    void givenNoVotes_whenCountDownvotesByPostId_thenReturnZero() {
        Optional<Long> actual = voteRepository.countDownvotesByPostId(3L);

        assertThat(actual).isEmpty();
    }

    @Test
    void givenVotes_whenCountUpvotesByPostIds_thenReturnTotalUpvotesForEachPost() {
        List<VoteCountByPostId> actual = voteRepository.countUpvotesByPostIds(Arrays.asList(1L, 2L));
        Map<Long, Long> mapped = actual.stream().collect(
                Collectors.toMap(VoteCountByPostId::getPostId, VoteCountByPostId::getVoteCount));

        assertThat(mapped).hasSize(2);
        assertThat(mapped.get(1L)).isEqualTo(2L);
        assertThat(mapped.get(2L)).isEqualTo(2L);
    }

    @Test
    void givenVotes_whenCountDownvotesByPostIds_thenReturnTotalDownvotesForEachPost() {
        List<VoteCountByPostId> actual = voteRepository.countDownvotesByPostIds(Arrays.asList(1L, 4L));
        Map<Long, Long> mapped = actual.stream().collect(
                Collectors.toMap(VoteCountByPostId::getPostId, VoteCountByPostId::getVoteCount));

        assertThat(mapped).hasSize(2);
        assertThat(mapped.get(1L)).isEqualTo(1L);
        assertThat(mapped.get(4L)).isEqualTo(1L);
    }

    @Test
    void givenUserDoesNotVote_whenGetVote_thenReturnEmptyOptional() {
        Optional<String> actual = voteRepository.getVote(2L, "user2");
        assertThat(actual).isEmpty();
    }

    @Test
    void givenUserUpvoted_whenGetVote_thenReturnOptionalWithValueUP() {
        Optional<String> actual = voteRepository.getVote(2L, "user1");
        assertThat(actual).hasValue("UP");
    }

    @Test
    void givenUserDownvoted_whenGetVote_thenReturnOptionalWithValueDOWN() {
        Optional<String> actual = voteRepository.getVote(1L, "user3");
        assertThat(actual).hasValue("DOWN");
    }
}
