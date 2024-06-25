package com.example.simpleforum.repositories;

import com.example.simpleforum.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
    This is an integration test using in memory H2 database that is created and destroyed during testing operation
    For how to create this, follow https://www.baeldung.com/spring-jpa-test-in-memory-database#data-model-and-repository
    And for how to use @Sql to seed data, see https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
    TODO: What if I use testcontainer to create PostgreSQL database instead?
 */
@DataJpaTest
@Sql({"/db/posts.sql"})
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    void givenPosts_whenGetPosts_thenReturnAllPosts() {
        List<Post> result = postRepository.findAll(10L);
        assertThat(result).isNotEmpty().hasSize(4);

        assertThat(result).allSatisfy(post -> {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getUserId()).isNotNull();
            assertThat(post.getTitle()).isNotNull();
            assertThat(post.getContent()).isNotNull();
            assertThat(post.getCreatedAt()).isNotNull();
            assertThat(post.getUpdatedAt()).isNotNull();
            assertThat(post.getDeletedAt()).isNull();
        });

        List<Long> postIds = result.stream().map(Post::getId).toList();
        assertThat(postIds).containsExactlyElementsOf(Arrays.asList(5L, 4L, 2L, 1L));
    }

    @Test
    void givenPosts_whenGetPostsWithLimitOfK_thenReturnFirstKPosts() {
        List<Post> result = postRepository.findAll(3L);
        assertThat(result).isNotEmpty().hasSize(3);

        assertThat(result).allSatisfy(post -> {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getUserId()).isNotNull();
            assertThat(post.getTitle()).isNotNull();
            assertThat(post.getContent()).isNotNull();
            assertThat(post.getCreatedAt()).isNotNull();
            assertThat(post.getUpdatedAt()).isNotNull();
            assertThat(post.getDeletedAt()).isNull();
        });

        List<Long> postIds = result.stream().map(Post::getId).toList();
        assertThat(postIds).containsExactlyElementsOf(Arrays.asList(5L, 4L, 2L));
    }

    @Test
    void givenPosts_whenGetPostsWithLastId_thenOnlyReturnPostsBeforeLastId() {
        List<Post> result = postRepository.findAll(3L, 5L);
        assertThat(result).isNotEmpty().hasSize(3);

        assertThat(result).allSatisfy(post -> {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getUserId()).isNotNull();
            assertThat(post.getTitle()).isNotNull();
            assertThat(post.getContent()).isNotNull();
            assertThat(post.getCreatedAt()).isNotNull();
            assertThat(post.getUpdatedAt()).isNotNull();
            assertThat(post.getDeletedAt()).isNull();
        });

        List<Long> postIds = result.stream().map(Post::getId).toList();
        assertThat(postIds).containsExactlyElementsOf(Arrays.asList(4L, 2L, 1L));
    }

    @Test
    void givenPosts_whenGetPostsByTitleKeyword_thenReturnAllMatchingPosts() {
        List<Post> result = postRepository.findByTitleKeyword("Post", 10L);
        assertThat(result).isNotEmpty().hasSize(2);

        List<Long> postIds = result.stream().map(Post::getId).toList();
        assertThat(postIds).containsExactlyElementsOf(Arrays.asList(2L, 1L));
    }

    @Test
    void givenPosts_whenGetPostsByTitleKeywordWithLimitOfK_thenReturnFirstKMatchingPosts() {
        List<Post> result = postRepository.findByTitleKeyword("Post", 1L);
        assertThat(result).isNotEmpty().hasSize(1);

        List<Long> postIds = result.stream().map(Post::getId).toList();
        assertThat(postIds).containsExactlyElementsOf(List.of(2L));
    }

    @Test
    void givenPosts_whenGetPostsByTitleKeywordWithLastId_thenReturnMatchingPostsAfterLastId() {
        List<Post> result = postRepository.findByTitleKeyword("Post", 1L, 2L);
        assertThat(result).isNotEmpty().hasSize(1);

        List<Long> postIds = result.stream().map(Post::getId).toList();
        assertThat(postIds).containsExactlyElementsOf(List.of(1L));
    }

    // @Test
    // void givenUserCreateNewPost_thenVerifyNewPostCreated() {
    //     List<Post> tmp = postRepository.findAll();
    //     Post newPost = new Post();
    //     newPost.setId(6L);  // Hack since H2 won't autoincrement new ID for me
    //     newPost.setUserId("user1");
    //     newPost.setTitle("New Title");
    //     newPost.setContent("New content");
    //     Instant currentInstant = Instant.now();
    //     postRepository.saveAndFlush(newPost);

    //     assertThat(newPost.getCreatedAt()).isAfter(currentInstant);
    //     assertThat(newPost.getUpdatedAt()).isAfter(currentInstant);
    //     assertThat(newPost.getDeletedAt()).isNull();
    // }
}
